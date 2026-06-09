package com.forage.service;

import com.forage.model.Demande;
import com.forage.model.StatutDemande;
import com.forage.repository.StatutDemandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional
public class StatutDemandeService  {

    @Autowired private StatutDemandeRepository statutDemandeRepository;
    
    // @Autowired private DevisRepository devisRepository;

    
    public List<StatutDemande> findAll() {
        return statutDemandeRepository.findAll();
    }
    @Transactional
    public void delete(int id) {
        StatutDemande current = statutDemandeRepository.findById(id).orElse(null);
        if (current == null) {
            return;
        }

        Demande demande = current.getDemande();
        int demandeId = demande != null ? demande.getId() : 0;
        LocalDateTime currentDate = current.getDateStatut();

        StatutDemande previous = null;
        StatutDemande next = null;
        if (demandeId > 0 && currentDate != null) {
            previous = statutDemandeRepository
                .findTopByDemandeIdAndDateStatutLessThanOrderByDateStatutDesc(demandeId, currentDate);
            next = statutDemandeRepository
                .findTopByDemandeIdAndDateStatutGreaterThanOrderByDateStatutAsc(demandeId, currentDate);
        }

        if (demande != null && demande.getStatutDemandes() != null) {
            demande.getStatutDemandes().remove(current);
        }

        statutDemandeRepository.delete(current);
        statutDemandeRepository.flush();

        if (next != null) {
            double nextDuree = 0.0;
            if (previous != null && previous.getDateStatut() != null && next.getDateStatut() != null) {
                nextDuree = computeWorkingMinutesExcludingWeekend(
                    previous.getDateStatut(),
                    next.getDateStatut()
                );
            }
            next.setDureeTravaille(nextDuree);
            statutDemandeRepository.save(next);
        }
    }
    

    public StatutDemande findById(int id) {
        return statutDemandeRepository.findById(id).orElse(null);
    }

    @Transactional
    public void updateDateAndObservationAndRecompute(int statutDemandeId, LocalDateTime newDate, String observations) {
        StatutDemande current = statutDemandeRepository.findById(statutDemandeId).orElse(null);
        if (current == null) {
            return;
        }

        current.setDateStatut(newDate);
        current.setObservations(observations);

        StatutDemande previous = statutDemandeRepository
            .findTopByDemandeIdAndDateStatutLessThanOrderByDateStatutDesc(
                current.getDemande().getId(),
                newDate
            );

        double currentDuree = 0.0;
        if (previous != null && previous.getDateStatut() != null) {
            currentDuree = computeWorkingMinutesExcludingWeekend(
                previous.getDateStatut(),
                newDate
            );
        }
        current.setDureeTravaille(currentDuree);
        statutDemandeRepository.save(current);

        StatutDemande next = statutDemandeRepository
            .findTopByDemandeIdAndDateStatutGreaterThanOrderByDateStatutAsc(
                current.getDemande().getId(),
                newDate
            );

        if (next != null && next.getDateStatut() != null) {
            double nextDuree = computeWorkingMinutesExcludingWeekend(
                newDate,
                next.getDateStatut()
            );
            next.setDureeTravaille(nextDuree);
            statutDemandeRepository.save(next);
        }
    }
    // @Transactional
    // public void changerStatut(StatutDemande nouvelHistorique) {
        
    //     Double dureeCalculee = 0.0;

    //     StatutDemande dernierHistorique = statutDemandeRepository
    //         .findTopByDemandeIdOrderByDateStatutDesc(nouvelHistorique.getDemande().getId());

    //     if (dernierHistorique != null && dernierHistorique.getDateStatut() != null) {
            
    //         long minutesEntre = computeWorkingMinutesExcludingWeekend(
    //             dernierHistorique.getDateStatut(),
    //             nouvelHistorique.getDateStatut()
    //         );
            
    //         dureeCalculee = (double) minutesEntre; 
    //     }

        
    //     nouvelHistorique.setDureeTravaille(dureeCalculee); // Enregistrement de la durée soustraite

    //     statutDemandeRepository.save(nouvelHistorique);
    // }
    @Transactional
public void changerStatut(StatutDemande nouvelHistorique) {
    Double dureeCalculee = 0.0;
    StatutDemande historiquePrecedent = null;

    if (nouvelHistorique.getId() == 0) {
        // MODE CRÉATION : Le précédent est le tout dernier enregistré
        historiquePrecedent = statutDemandeRepository
            .findTopByDemandeIdOrderByDateStatutDesc(nouvelHistorique.getDemande().getId());
    } else {
        // MODE MODIFICATION : Le précédent est le dernier enregistré AVANT celui-ci
        historiquePrecedent = statutDemandeRepository
            .findTopByDemandeIdAndIdNotOrderByDateStatutDesc(
                nouvelHistorique.getDemande().getId(), 
                nouvelHistorique.getId()
            );
    }

    // Calcul de la durée par rapport à l'historique précédent (s'il existe)
    if (historiquePrecedent != null && historiquePrecedent.getDateStatut() != null) {
        long minutesEntre = computeWorkingMinutesExcludingWeekend(
            historiquePrecedent.getDateStatut(),
            nouvelHistorique.getDateStatut()
        );
        dureeCalculee = (double) minutesEntre; 
    }

    // Attribution de la durée recalculée
    nouvelHistorique.setDureeTravaille(dureeCalculee);

    // Sauvegarde (Spring/Hibernate fera un UPDATE si l'ID existe, ou un INSERT s'il vaut 0)
    statutDemandeRepository.save(nouvelHistorique);
}

    static long computeWorkingMinutesExcludingWeekend(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return 0L;
        }
        if (!end.isAfter(start)) {
            return 0L;
        }

        // Working hours: 08:00 (inclusive) to 16:00 (exclusive/end)
        final LocalTime WORK_START = LocalTime.of(8, 0);
        final LocalTime WORK_END = LocalTime.of(16, 0);

        long totalMinutes = 0L;
        LocalDate currentDate = start.toLocalDate();
        LocalDate endDate = end.toLocalDate();

        while (!currentDate.isAfter(endDate)) {
            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
            boolean isWeekend = dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;

            if (!isWeekend) {
                LocalDateTime dayWorkStart = currentDate.atTime(WORK_START);
                LocalDateTime dayWorkEnd = currentDate.atTime(WORK_END);

                // The effective segment for this day is the intersection of [start,end] and [dayWorkStart, dayWorkEnd]
                LocalDateTime segmentStart = start.isAfter(dayWorkStart) ? start : dayWorkStart;
                LocalDateTime segmentEnd = end.isBefore(dayWorkEnd) ? end : dayWorkEnd;

                if (segmentEnd.isAfter(segmentStart)) {
                    totalMinutes += ChronoUnit.MINUTES.between(segmentStart, segmentEnd);
                }
            }

            currentDate = currentDate.plusDays(1);
        }

        return totalMinutes;
    }

    // public Demande findByReference(String reference) {
    //     return demandeRepository.findByReference(reference).orElse(null);
    // }

    
    public void save(StatutDemande demande) {
        statutDemandeRepository.save(demande);
    }
    // @Transactional
    // public void delete(int id) {
    
    //     Demande demande = demandeRepository.findById(id).orElse(null);
        
    //     if (demande != null) {
            
    //         statutDemandeRepository.deleteByDemandeId(id); 
    //         devisRepository.deleteByDemandeId(id); 
        
    //         demandeRepository.delete(demande);
    //     }
    // }

    // public void delete(int id) {
    //     demandeRepository.deleteById(id);
    // }
}
