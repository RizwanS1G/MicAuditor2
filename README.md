# Mic Permission Auditor

Ek simple Android app jo batati hai ki aapke phone mein **kis-kis app ke paas
microphone permission granted hai** — chahe wo abhi active ho ya nahi.

## Ye kya karta hai
- Har installed app ko scan karta hai
- Public `PackageManager` API use karke check karta hai ki kis app ko
  `RECORD_AUDIO` permission mili hui hai
- List dikhata hai — tap karke seedha us app ki Settings pe jaake
  permission revoke kar sakte ho

## Ye kya NAHI karta (important)
Normal Android apps ko OS **real-time mein ye nahi dekhne deta ki koi app
is-waqt mic use kar rahi hai ya nahi** (ye special system-level permission
maangta hai, jo Play Store apps ko nahi milti). Ye app sirf ye batata hai ki
**kisko permission di hui hai** — jo ki asli risk hai, kyunki jis app ke
paas permission nahi hai, wo mic use hi nahi kar sakti.

Real-time "jab bhi mic use ho tab turant notify karo" ke liye Shizuku ya
root chahiye hoga (jo aapne skip karna chaha).

## Build kaise karein
1. [Android Studio](https://developer.android.com/studio) install karo
2. `File → Open` → ye `MicAuditor` folder select karo
3. Gradle sync hone do (pehli baar thoda time lagega)
4. `Run ▶` dabao (phone USB se connect karo, Developer Options → USB
   Debugging on karo) — ya `Build → Build Bundle(s)/APK(s) → Build APK(s)`
   se seedha APK bana ke phone mein manually install kar sakte ho

## Use kaise karein
1. App kholo
2. List mein wo saari apps dikhengi jinke paas mic permission hai
3. Kisi bhi app pe tap karo → seedha uski App Info settings khulegi jahan
   se aap "Microphone" permission ko **Deny/Ask every time** kar sakte ho
4. Jis app ko mic ki genuinely zaroorat nahi (jaise calculator, flashlight,
   wallpaper app), uski permission turant hata do

## Suggestion
Isko regularly (jaise hafte mein ek baar) khol ke check karte raho — jab
bhi koi naya app install karo aur wo mic maange, socho ki kya usse genuinely
zaroorat hai.
