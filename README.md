# 📱 Application mobile – Projet Intégrateur CUT
> Kotlin • Android • WebSockets • MIT

## 🇫🇷 Présentation
Cette application Android permet aux spectateurs de suivre en temps réel les matchs, les équipes et les statistiques de notre projet intégrateur CUT.

| Clé | Valeur |
|-----|--------|
| **Langage** | Kotlin |
| **minSdk / targetSdk** | 24 / 35 *(suggestion pour couvrir ≈ 93 % des appareils actifs en 2025 tout en profitant des dernières API)* |
| **Communication** | WebSockets (Socket.IO / OkHttp) |
| **Licence** | MIT |
| **Auteurs** | Damien C., Christoph M., Gabriel H., Gabriel B., Johan M. L., Moad L. |

## Fonctionnalités
- 🔴 Scores et minuteurs en direct  
- 🔍 Recherche d’équipes et filtrage  
- 🔔 Notifications push *(🟡 TODO: service FCM?)*  
- 📶 Mode hors-connexion cache/refresh  

## Architecture (🟡 TODO)
Nous viserons un MVVM moderne : ViewModel + StateFlow, Jetpack Compose UI, Hilt DI, Room cache local.

## Installation développeur
```bash
git clone https://github.com/ProjetImproCUT/ProjetIntegrateurCUT-AppliMobile
./gradlew assembleDebug
