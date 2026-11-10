# MMF (My Money Flow) v2.0

Android live wallpaper with once-per-second redraw while visible, numeric-only wallpaper, configurable X/Y/size, minimalist dashboard, payday progress, and a local congratulations animation.

## Build
GitHub Actions: `.github/workflows/build.yml` -> Build APK. Debug APK is in workflow artifacts. Android SDK 35 and JDK 17 required.

## Important limitations
- **Friends and messaging are not implemented**. Secure messaging requires authentication, a server/database, privacy controls and abuse prevention. No salary data is sent to any server by this app.
- Standard Android launcher widgets are **not implemented**; a 1-second widget update cannot be guaranteed by Android. The **live wallpaper** displays only the number and refreshes once a second while visible.
- The payday receipt button plays a local celebration only; it does not alter pay cycle accounting.
- This package uses the same application ID as the previous project, but signing keys must match to install as an update; otherwise uninstall the old app first (settings may be lost).
- Not device-tested or APK-compiled in this environment.
