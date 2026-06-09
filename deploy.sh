#!/usr/bin/env bash
set -euo pipefail

# deploy.sh - déploie le WAR vers Tomcat (Linux)
# Usage:
#   APP_NAME=myapp TOMCAT_WEBAPPS=/path/to/tomcat/webapps ./deploy.sh

APP_NAME=${APP_NAME:-forage}
# Défaut courant, adapte si besoin (tomcat9/tomcat10 selon installation)
if [ -z "${TOMCAT_WEBAPPS:-}" ] && [ -d "/opt/lampp/tomcat/webapps" ]; then
  TOMCAT_WEBAPPS="/opt/lampp/tomcat/webapps"
else
  TOMCAT_WEBAPPS=${TOMCAT_WEBAPPS:-/home/edinah/Documents/logiciel/tomcat/webapps}
fi

MVN_CMD=${MVN_CMD:-mvn}
SKIP_TESTS=${SKIP_TESTS:-true}

WAR_BASENAME="${APP_NAME}-1.0-SNAPSHOT.war"
TARGET_WAR="target/${WAR_BASENAME}"
DEST_WAR="${TOMCAT_WEBAPPS}/${APP_NAME}.war"

echo "[1/4] Vérifications préalables"
command -v "$MVN_CMD" >/dev/null 2>&1 || { echo "$MVN_CMD introuvable. Installez Maven ou définissez MVN_CMD."; exit 1; }

if [ ! -d "${TOMCAT_WEBAPPS}" ]; then
  echo "Répertoire Tomcat webapps introuvable: ${TOMCAT_WEBAPPS}"
  echo "Modifiez la variable TOMCAT_WEBAPPS ou créez le répertoire si nécessaire."
  exit 1
fi

echo "[2/4] Nettoyage et compilation avec Maven"
if [ "${SKIP_TESTS}" = "true" ]; then
  "$MVN_CMD" clean package -DskipTests
else
  "$MVN_CMD" clean package
fi

if [ ! -f "${TARGET_WAR}" ]; then
  echo "Fichier WAR ${TARGET_WAR} introuvable, recherche d'un WAR alternatif dans target/ ..."
  # Cherche d'abord un WAR nommé forage.war puis tout autre *.war
  if [ -f "target/${APP_NAME}.war" ]; then
    TARGET_WAR="target/${APP_NAME}.war"
    echo "Trouvé: ${TARGET_WAR}"
  else
    # trouver le premier .war dans target/
    war_candidate=$(ls -1 target/*.war 2>/dev/null | head -n 1 || true)
    if [ -n "${war_candidate}" ]; then
      TARGET_WAR="${war_candidate}"
      echo "Utilisation du WAR trouvé: ${TARGET_WAR}"
    else
      echo "Aucun fichier .war trouvé dans target/.";
      exit 1
    fi
  fi
fi

echo "[3/4] Déploiement vers Tomcat: ${DEST_WAR}"
# Sauvegarde de l'ancien WAR si présent
if [ -f "${DEST_WAR}" ]; then
  timestamp=$(date +%Y%m%d%H%M%S)
  backup="${DEST_WAR}.bak.${timestamp}"
  echo "Sauvegarde de l'ancien WAR en ${backup}"
  cp -v "${DEST_WAR}" "${backup}"
fi

cp -v "${TARGET_WAR}" "${DEST_WAR}"

# Si Tomcat est géré par systemd et que l'on a les droits, on peut redémarrer pour forcer le déploiement
if command -v systemctl >/dev/null 2>&1 && [ $(id -u) -eq 0 ]; then
  echo "Tentative de redémarrage de Tomcat via systemctl (nécessite d'être root)"
  # On essaye les noms communs
  for svc in tomcat tomcat9 tomcat10; do
    if systemctl is-enabled "$svc" >/dev/null 2>&1; then
      echo "Redémarrage du service $svc"
      systemctl restart "$svc"
      echo "Redémarrage demandé pour $svc"
      break
    fi
  done
fi

echo "[4/4] Terminé. Accédez à http://localhost:8080/${APP_NAME}/ (attendez que Tomcat déploie le WAR)"
