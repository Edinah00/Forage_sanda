package com.forage.repository;

import com.forage.dto.AlertDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import com.forage.model.*;
@Repository
public class AlertRepository {

    private final JdbcTemplate jdbcTemplate;

    public AlertRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // public List<AlertDto> findAlerts() {
    //     String sql = "SELECT p.idStatut1, p.idStatut2, p.duree AS seuil_duree, p.alerte, "
    //         + "COALESCE(SUM(sd.dureeTravaille), 0) AS duree_travaille "
    //         + "FROM Parametre p "
    //         + "LEFT JOIN StatutDemande sd "
    //         //+ "ON sd.idDemande = ? "
    //         + "AND sd.idStatut > LEAST(p.idStatut1, p.idStatut2) "
    //         + "AND sd.idStatut <= GREATEST(p.idStatut1, p.idStatut2) "
    //         + "GROUP BY p.idStatut1, p.idStatut2, p.duree, p.alerte "
    //         + "HAVING COALESCE(SUM(sd.dureeTravaille), 0) >= p.duree "
    //         + "ORDER BY p.idStatut1, p.idStatut2";

    //     return jdbcTemplate.query(sql,
    //         (rs, rowNum) -> new AlertDto(new Parametre(

    //             new Statut(rs.getInt("idStatut1"),null),
    //             new Statut(rs.getInt("idStatut2"),null),
    //             rs.getInt("seuil_duree"),
    //             rs.getString("alerte")
    //             ),
    //             rs.getDouble("duree_travaille")
    //         )
    //     );
    // }
}
