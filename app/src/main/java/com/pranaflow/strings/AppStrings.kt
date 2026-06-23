package com.pranaflow.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import com.pranaflow.model.BreathingTechnique
import com.pranaflow.model.TechniqueCategory

data class AppStrings(
    val settings: String,
    val pranaFlow: String,
    val voiceGuidance: String,
    val spokenBreathingCues: String,
    val voice: String,
    val language: String,
    val english: String,
    val hindi: String,
    val ambientSound: String,
    val ambient: String,
    val backgroundMusic: String,
    val backgroundTrack: String,
    val hapticFeedback: String,
    val gentleVibration: String,
    val darkMode: String,
    val darkThemeActive: String,
    val lightThemeActive: String,
    val glowIntensity: String,
    val sacredGeometryLuminance: String,
    val soundSection: String,
    val feedbackSection: String,
    val visualSection: String,
    val inhale: String,
    val hold: String,
    val exhale: String,
    val ready: String,
    val repeatTheMantra: String,
    val thisMusicWillHealYou: String,
    val letTheMusicHeal: String,
    val cycle: String,
    val customTiming: String,
    val start: String,
    val pause: String,
    val cancel: String,
    val breatheWithRhythm: String,
    val quickCalm: String,
    val soon: String,
    val welcome: String,
    val welcomeDesc: String,
    val chooseYourPath: String,
    val choosePathDesc: String,
    val justBreathe: String,
    val justBreatheDesc: String,
    val getStarted: String,
    val swipeToContinue: String,
    val relaxCategory: String,
    val relaxSubtitle: String,
    val focusCategory: String,
    val focusSubtitle: String,
    val nadiShodhan: String,
    val nadiSubtitle: String,
    val chakrasCategory: String,
    val chakrasSubtitle: String,
    val mantraCategory: String,
    val mantraSubtitle: String,
    val comingSoon: String,
    val boxBreathingName: String,
    val breathing478Name: String,
    val bhramariName: String,
    val dirgaName: String,
    val ujjayiName: String,
    val slowBreathingName: String,
    val anulomVilomName: String,
    val equalRhythm: String,
    val deepRelaxation: String,
    val beeBreath: String,
    val threePartBreath: String,
    val oceanBreath: String,
    val deepInhaleExhale: String,
    val alternateNostril: String,
    val muladharaName: String,
    val svadhisthanaName: String,
    val manipuraName: String,
    val anahataName: String,
    val vishuddhaName: String,
    val ajnaName: String,
    val sahasraraName: String,
    val omChant: String,
    val rainThunder: String,
    val forestNight: String,
    val singingBowls: String,
    val flute: String,
    val rootSubtitle: String,
    val sacralSubtitle: String,
    val solarSubtitle: String,
    val heartSubtitle: String,
    val throatSubtitle: String,
    val thirdEyeSubtitle: String,
    val crownSubtitle: String,
    val rootDesc: String,
    val sacralDesc: String,
    val solarDesc: String,
    val heartDesc: String,
    val throatDesc: String,
    val thirdEyeDesc: String,
    val crownDesc: String,
    val sahasraraNote: String,
    val allChakrasName: String,
    val allChakrasSubtitle: String,
    val allChakrasDesc: String,
    // ── Music Healing ──
    val music174hzName: String,
    val music174hzSubtitle: String,
    val music285hzName: String,
    val music285hzSubtitle: String,
    val music396hzName: String,
    val music396hzSubtitle: String,
    val music417hzName: String,
    val music417hzSubtitle: String,
    val music432hzName: String,
    val music432hzSubtitle: String,
    val music528hzName: String,
    val music528hzSubtitle: String,
    val music639hzName: String,
    val music639hzSubtitle: String,
    val music741hzName: String,
    val music741hzSubtitle: String,
    val music852hzName: String,
    val music852hzSubtitle: String,
    val music963hzName: String,
    val music963hzSubtitle: String,
    val music174hzDesc: String,
    val music285hzDesc: String,
    val music396hzDesc: String,
    val music417hzDesc: String,
    val music432hzDesc: String,
    val music528hzDesc: String,
    val music639hzDesc: String,
    val music741hzDesc: String,
    val music852hzDesc: String,
    val music963hzDesc: String
)

val EnglishStrings = AppStrings(
    settings = "SETTINGS",
    pranaFlow = "PRANA FLOW",
    voiceGuidance = "Voice Guidance",
    spokenBreathingCues = "Spoken breathing cues",
    voice = "VOICE",
    language = "Language",
    english = "ENGLISH",
    hindi = "HINDI",
    ambientSound = "Ambient Sound",
    ambient = "AMBIENT",
    backgroundMusic = "Background music",
    backgroundTrack = "Background Track",
    hapticFeedback = "Haptic Feedback",
    gentleVibration = "Gentle vibration on phase change",
    darkMode = "Dark Mode",
    darkThemeActive = "Dark theme active",
    lightThemeActive = "Light theme active",
    glowIntensity = "Glow Intensity",
    sacredGeometryLuminance = "Sacred geometry luminance",
    soundSection = "SOUND",
    feedbackSection = "FEEDBACK",
    visualSection = "VISUAL",
    inhale = "Inhale",
    hold = "Hold",
    exhale = "Exhale",
    ready = "Ready",
    repeatTheMantra = "REPEAT THE MANTRA",
    thisMusicWillHealYou = "This music will heal you",
    letTheMusicHeal = "LET THE MUSIC HEAL YOU",
    cycle = "Cycle",
    customTiming = "CUSTOM TIMING",
    start = "START",
    pause = "PAUSE",
    cancel = "CANCEL",
    breatheWithRhythm = "Breathe with Rhythm",
    quickCalm = "Quick Calm · 1 min",
    soon = "SOON",
    welcome = "Welcome",
    welcomeDesc = "Ancient breathing wisdom\nfor modern life.\nCalm your mind in seconds.",
    chooseYourPath = "Choose Your Path",
    choosePathDesc = "Pranayam for stress relief,\nchakra meditation for energy,\nor focused breathing for clarity.",
    justBreathe = "Just Breathe",
    justBreatheDesc = "No login. No clutter.\nTap a technique and follow\nthe sacred geometry.",
    getStarted = "GET STARTED",
    swipeToContinue = "SWIPE TO CONTINUE →",
    relaxCategory = "Relax / Stress Relief",
    relaxSubtitle = "Calm your nervous system",
    focusCategory = "Focus Boost",
    focusSubtitle = "Sharpen your awareness",
    nadiShodhan = "Nadi Shodhan",
    nadiSubtitle = "Purify energy channels",
    chakrasCategory = "7 Chakras",
    chakrasSubtitle = "Awaken your energy centers",
    mantraCategory = "Music Healing",
    mantraSubtitle = "Solfeggio frequencies for healing",
    comingSoon = "Coming soon",
    boxBreathingName = "4-4-4-4 Kumbhak",
    breathing478Name = "4-7-8 Kumbhak",
    bhramariName = "Bhramari",
    dirgaName = "Dirga Pranayam",
    ujjayiName = "Ujjayi",
    slowBreathingName = "Slow Breathing",
    anulomVilomName = "Anulom Vilom",
    equalRhythm = "Equal rhythm, deep calm",
    deepRelaxation = "Deep relaxation technique",
    beeBreath = "Bee breath, heals anxiety",
    threePartBreath = "Three-part healing breath",
    oceanBreath = "Ocean breath, inner fire",
    deepInhaleExhale = "Deep inhale, deep exhale",
    alternateNostril = "Alternate nostril balance",
    muladharaName = "Muladhara",
    svadhisthanaName = "Svadhisthana",
    manipuraName = "Manipura",
    anahataName = "Anahata",
    vishuddhaName = "Vishuddha",
    ajnaName = "Ajna",
    sahasraraName = "Sahasrara",
    omChant = "Om Chant",
    rainThunder = "Rain & Thunder",
    forestNight = "Forest Night",
    singingBowls = "Singing Bowls",
    flute = "Flute",
    rootSubtitle = "Root · Grounding & stability",
    sacralSubtitle = "Sacral · Creativity & flow",
    solarSubtitle = "Solar Plexus · Power & will",
    heartSubtitle = "Heart · Love & compassion",
    throatSubtitle = "Throat · Expression & truth",
    thirdEyeSubtitle = "Third Eye · Intuition & insight",
    crownSubtitle = "Crown · Consciousness & bliss",
    rootDesc = "Located at the base of the spine; it provides a sense of stability, security, and groundedness.",
    sacralDesc = "Located in the lower abdomen; it governs your creativity, emotions, and sexual energy.",
    solarDesc = "Located in the stomach area; it builds self-confidence, willpower, and personal power.",
    heartDesc = "Located in the center of the chest; it is the center for love, compassion, and emotional healing.",
    throatDesc = "Located in the throat; it helps with clear communication and expressing your truth.",
    thirdEyeDesc = "Located between the eyebrows; it enhances intuition, imagination, and mental focus.",
    crownDesc = "Located at the top of the head; it connects you to spiritual consciousness and inner peace.",
    sahasraraNote = "In this practice, there is no mantra chanting; instead, one listens to the spontaneously resonating inner 'OM' sound.",
    allChakrasName = "All 7 Chakras",
    allChakrasSubtitle = "Complete chakra healing",
    allChakrasDesc = "Listen to this music to heal all your 7 chakras at once.",
    // ── Music Healing ──
    music174hzName = "174Hz Pain Release",
    music174hzSubtitle = "Foundation frequency · Reduces pain",
    music285hzName = "285Hz Influence Energy Field",
    music285hzSubtitle = "Healing frequency · Restores tissues",
    music396hzName = "396Hz Liberates Fear & Guilt",
    music396hzSubtitle = "Liberation frequency · Releases fear",
    music417hzName = "417Hz Facilitates Change",
    music417hzSubtitle = "Change frequency · Clears negativity",
    music432hzName = "432Hz Miracle Tone Of Nature",
    music432hzSubtitle = "Nature frequency · Deep harmony",
    music528hzName = "528Hz Repairs DNA",
    music528hzSubtitle = "Love frequency · DNA repair",
    music639hzName = "639Hz Heals Relationships",
    music639hzSubtitle = "Connection frequency · Harmonizes",
    music741hzName = "741Hz Awaken Intuition",
    music741hzSubtitle = "Intuition frequency · Cleanses cells",
    music852hzName = "852Hz Attract Soul Tribe",
    music852hzSubtitle = "Soul frequency · Awakens inner order",
    music963hzName = "963Hz Connect With Light & Spirit",
    music963hzSubtitle = "Divine frequency · Pure consciousness",
    music174hzDesc = "The lowest Solfeggio frequency. It acts as a natural anesthetic, reducing pain and giving a sense of safety and love.",
    music285hzDesc = "Influences energy fields and helps heal cuts, burns and damaged tissues. Sends a message to restructure damaged organs and tissues.",
    music396hzDesc = "Liberates you from feelings of fear and guilt. It cleanses feelings of guilt that often represent one of the basic obstacles to realization.",
    music417hzDesc = "Facilitates change and undoes situations. It clears traumatic experiences and cleanses destructive influences of past events.",
    music432hzDesc = "Known as the miracle tone of nature, 432Hz fills the mind with a sense of peace and well-being. It aligns you with the heartbeat of the universe.",
    music528hzDesc = "Known as the Love Frequency. It is central to the musical mathematical matrix of creation and is said to repair DNA and restore human consciousness to its original state.",
    music639hzDesc = "Enables creation of harmonious community and harmonious interpersonal relationships. It can be used for dealing with relationship problems.",
    music741hzDesc = "Leads to a purer, stable and spiritual life. It cleanses the cells from electromagnetic radiation and toxins. It awakens intuition and leads to a healthier, simpler life.",
    music852hzDesc = "Directly connected to the principle of Light. It enables you to see through illusions of life and awakens intuition to return to spiritual order and attract your soul tribe.",
    music963hzDesc = "Associated with the Sahasrara chakra and awakening of perfect state. It connects you with the Light and all-embracing Spirit, enabling direct experience of the return to Oneness."
)

val HindiStrings = AppStrings(
    settings = "सेटिंग्स",
    pranaFlow = "प्राण प्रवाह",
    voiceGuidance = "स्वर मार्गदर्शन",
    spokenBreathingCues = "श्वास निर्देश",
    voice = "आवाज़",
    language = "भाषा",
    english = "अंग्रेज़ी",
    hindi = "हिन्दी",
    ambientSound = "वातावरण ध्वनि",
    ambient = "वातावरण",
    backgroundMusic = "पृष्ठभूमि संगीत",
    backgroundTrack = "पृष्ठभूमि ट्रैक",
    hapticFeedback = "स्पर्श प्रतिक्रिया",
    gentleVibration = "चरण बदलने पर हल्का कंपन",
    darkMode = "डार्क मोड",
    darkThemeActive = "डार्क थीम सक्रिय",
    lightThemeActive = "लाइट थीम सक्रिय",
    glowIntensity = "प्रकाश तीव्रता",
    sacredGeometryLuminance = "पवित्र ज्यामिति चमक",
    soundSection = "ध्वनि",
    feedbackSection = "प्रतिक्रिया",
    visualSection = "दृश्य",
    inhale = "श्वास लें",
    hold = "रोकें",
    exhale = "श्वास छोड़ें",
    ready = "तैयार",
    repeatTheMantra = "मंत्र दोहराएँ",
    thisMusicWillHealYou = "यह ध्वनि आपको ठीक करेगा",
    letTheMusicHeal = "संगीत को ठीक करने दें",
    cycle = "चक्र",
    customTiming = "कस्टम समय",
    start = "प्रारंभ करें",
    pause = "विराम",
    cancel = "रद्द करें",
    breatheWithRhythm = "लय के साथ श्वास लें",
    quickCalm = "त्वरित शांति · 1 मिनट",
    soon = "जल्द आ रहा है",
    welcome = "स्वागत है",
    welcomeDesc = "आधुनिक जीवन के लिए\nप्राचीन श्वास ज्ञान।\nकुछ ही सेकंड में मन को शांत करें।",
    chooseYourPath = "अपना मार्ग चुनें",
    choosePathDesc = "तनाव मुक्ति के लिए प्राणायाम,\nऊर्जा के लिए चक्र ध्यान,\nया स्पष्टता के लिए केंद्रित श्वास।",
    justBreathe = "बस श्वास लें",
    justBreatheDesc = "न लॉगिन, न कोई उलझन।\nएक तकनीक चुनें और\nपवित्र ज्यामिति का अनुसरण करें।",
    getStarted = "शुरू करें",
    swipeToContinue = "आगे बढ़ने के लिए स्वाइप करें →",
    relaxCategory = "आराम / तनाव मुक्ति",
    relaxSubtitle = "अपने तंत्रिका तंत्र को शांत करें",
    focusCategory = "एकाग्रता बढ़ाएँ",
    focusSubtitle = "अपनी जागरूकता बढ़ाएँ",
    nadiShodhan = "नाड़ी शोधन",
    nadiSubtitle = "ऊर्जा मार्गों को शुद्ध करें",
    chakrasCategory = "7 चक्र",
    chakrasSubtitle = "अपने ऊर्जा केंद्र जागृत करें",
    mantraCategory = "ध्वनि उपचार",
    mantraSubtitle = "उपचार के लिए सोलफेजियो फ्रीक्वेंसी",
    comingSoon = "जल्द आ रहा है",
    boxBreathingName = "4-4-4-4 कुम्भक",
    breathing478Name = "4-7-8 कुम्भक",
    bhramariName = "भ्रामरी",
    dirgaName = "दीर्घ प्राणायाम",
    ujjayiName = "उज्जयी",
    slowBreathingName = "धीमी श्वास",
    anulomVilomName = "अनुलोम विलोम",
    equalRhythm = "समान लय, गहरी शांति",
    deepRelaxation = "गहरी विश्रांति तकनीक",
    beeBreath = "भ्रामरी श्वास, मन को शांत करे",
    threePartBreath = "त्रि-स्तरीय उपचारात्मक श्वास",
    oceanBreath = "महासागर श्वास, आंतरिक ऊर्जा",
    deepInhaleExhale = "गहरी श्वास लें, गहरी श्वास छोड़ें",
    alternateNostril = "वैकल्पिक नासिका संतुलन",
    muladharaName = "मूलाधार",
    svadhisthanaName = "स्वाधिष्ठान",
    manipuraName = "मणिपुर",
    anahataName = "अनाहत",
    vishuddhaName = "विशुद्धि",
    ajnaName = "आज्ञा",
    sahasraraName = "सहस्रार",
    omChant = "ॐ मंत्र जाप",
    rainThunder = "वर्षा और गरज",
    forestNight = "वन रात्रि",
    singingBowls = "सिंगिंग बाउल्स",
    flute = "बांसुरी",
    rootSubtitle = "मूल · स्थिरता और आधार",
    sacralSubtitle = "त्रिक · रचनात्मकता और प्रवाह",
    solarSubtitle = "मणिपुर · शक्ति और इच्छा",
    heartSubtitle = "हृदय · प्रेम और करुणा",
    throatSubtitle = "कंठ · अभिव्यक्ति और सत्य",
    thirdEyeSubtitle = "तृतीय नेत्र · अंतर्ज्ञान और अंतर्दृष्टि",
    crownSubtitle = "सहस्रार · चेतना और आनंद",
    rootDesc = "रीढ़ की हड्डी के नीचे स्थित; यह स्थिरता, सुरक्षा और जमीन से जुड़ाव प्रदान करता है।",
    sacralDesc = "पेट के निचले हिस्से में स्थित; यह रचनात्मकता, भावनाओं और यौन ऊर्जा को नियंत्रित करता है।",
    solarDesc = "पेट के क्षेत्र में स्थित; यह आत्मविश्वास, इच्छाशक्ति और व्यक्तिगत शक्ति बढ़ाता है।",
    heartDesc = "छाती के केंद्र में स्थित; यह प्रेम, करुणा और भावनात्मक उपचार का केंद्र है।",
    throatDesc = "गले में स्थित; यह स्पष्ट संवाद और सत्य की अभिव्यक्ति में सहायता करता है।",
    thirdEyeDesc = "भौंहों के बीच स्थित; यह अंतर्ज्ञान, कल्पना और मानसिक एकाग्रता को बढ़ाता है।",
    crownDesc = "सिर के शीर्ष पर स्थित; यह आध्यात्मिक चेतना और आंतरिक शांति से जोड़ता है।",
    sahasraraNote = "इस प्रक्रिया में मंत्र जाप नहीं, बल्कि स्वतः गूंजने वाली आंतरिक 'ॐ' की ध्वनि को सुनना होता है।",
    allChakrasName = "संपूर्ण 7 चक्र",
    allChakrasSubtitle = "संपूर्ण चक्र उपचार",
    allChakrasDesc = "इस संगीत को सुनें और अपने सातों चक्रों को एक साथ जागृत करें।",
    // ── Music Healing — Hindi ──
    music174hzName = "174Hz दर्द से मुक्ति",
    music174hzSubtitle = "आधार फ्रीक्वेंसी · दर्द कम करे",
    music285hzName = "285Hz ऊर्जा क्षेत्र को प्रभावित करे",
    music285hzSubtitle = "उपचार फ्रीक्वेंसी · ऊतकों को पुनर्स्थापित करे",
    music396hzName = "396Hz भय और अपराधबोध से मुक्ति",
    music396hzSubtitle = "मुक्ति फ्रीक्वेंसी · भय को दूर करे",
    music417hzName = "417Hz परिवर्तन को सुगम बनाए",
    music417hzSubtitle = "परिवर्तन फ्रीक्वेंसी · नकारात्मकता दूर करे",
    music432hzName = "432Hz प्रकृति का चमत्कारी स्वर",
    music432hzSubtitle = "प्राकृतिक फ्रीक्वेंसी · गहरी समरसता",
    music528hzName = "528Hz डीएनए की मरम्मत",
    music528hzSubtitle = "प्रेम फ्रीक्वेंसी · डीएनए उपचार",
    music639hzName = "639Hz रिश्तों को ठीक करे",
    music639hzSubtitle = "जुड़ाव फ्रीक्वेंसी · सामंजस्य लाए",
    music741hzName = "741Hz अंतर्ज्ञान को जगाए",
    music741hzSubtitle = "अंतर्ज्ञान फ्रीक्वेंसी · कोशिकाओं को शुद्ध करे",
    music852hzName = "852Hz आत्म-समूह को आकर्षित करे",
    music852hzSubtitle = "आत्मा फ्रीक्वेंसी · आंतरिक व्यवस्था जगाए",
    music963hzName = "963Hz प्रकाश और आत्मा से जुड़ें",
    music963hzSubtitle = "दिव्य फ्रीक्वेंसी · शुद्ध चेतना",
    music174hzDesc = "यह सबसे कम सोलफेजियो फ्रीक्वेंसी है। यह एक प्राकृतिक दर्दनिवारक की तरह काम करती है, दर्द को कम करती है और सुरक्षा व प्रेम का भाव जगाती है।",
    music285hzDesc = "यह ऊर्जा क्षेत्र को प्रभावित करती है और कटने, जलने तथा क्षतिग्रस्त ऊतकों को ठीक करने में मदद करती है। यह अंगों और ऊतकों को पुनर्निर्मित करने का संदेश भेजती है।",
    music396hzDesc = "यह आपको भय और अपराधबोध की भावनाओं से मुक्त करती है। यह अपराधबोध को शुद्ध करती है जो प्रायः आत्म-साक्षात्कार में सबसे बड़ी बाधा होती है।",
    music417hzDesc = "यह परिवर्तन को सुगम बनाती है और कठिन परिस्थितियों को बदलती है। यह दर्दनाक अनुभवों को साफ करती है और पुरानी नकारात्मक घटनाओं के प्रभाव को दूर करती है।",
    music432hzDesc = "432Hz को प्रकृति का चमत्कारी स्वर कहा जाता है। यह मन को शांति और खुशहाली से भर देती है तथा आपको ब्रह्मांड की धड़कन के साथ जोड़ती है।",
    music528hzDesc = "इसे प्रेम फ्रीक्वेंसी कहा जाता है। यह सृष्टि के संगीतमय गणितीय आधार का केंद्र है और माना जाता है कि यह डीएनए की मरम्मत कर मानव चेतना को उसके मूल स्वरूप में लाती है।",
    music639hzDesc = "यह सामंजस्यपूर्ण समुदाय और स्वस्थ पारस्परिक संबंधों के निर्माण को सक्षम बनाती है। इसका उपयोग रिश्तों की समस्याओं को सुलझाने में किया जा सकता है।",
    music741hzDesc = "यह एक शुद्ध, स्थिर और आध्यात्मिक जीवन की ओर ले जाती है। यह कोशिकाओं को विद्युत-चुम्बकीय विकिरण और विषाक्त पदार्थों से शुद्ध करती है और अंतर्ज्ञान को जगाती है।",
    music852hzDesc = "यह प्रकाश के सिद्धांत से सीधे जुड़ी है। यह जीवन के भ्रमों को देखने और आध्यात्मिक व्यवस्था में लौटने की शक्ति देती है तथा आत्मीय लोगों को आकर्षित करती है।",
    music963hzDesc = "यह सहस्रार चक्र और परम जागृति से जुड़ी है। यह आपको प्रकाश और सर्वव्यापी आत्मा से जोड़ती है तथा एकत्व की प्रत्यक्ष अनुभूति कराती है।"
)

val LocalAppStrings = staticCompositionLocalOf { EnglishStrings }

object S {
    val current: AppStrings
        @Composable get() = LocalAppStrings.current
}

@Composable
fun TechniqueCategory.localizedLabel(): String {
    val s = S.current
    return when (this) {
        TechniqueCategory.RELAX -> s.relaxCategory
        TechniqueCategory.FOCUS -> s.focusCategory
        TechniqueCategory.NADI_SHODHAN -> s.nadiShodhan
        TechniqueCategory.CHAKRAS -> s.chakrasCategory
        TechniqueCategory.MANTRA -> s.mantraCategory
    }
}

@Composable
fun TechniqueCategory.localizedSubtitle(): String {
    val s = S.current
    return when (this) {
        TechniqueCategory.RELAX -> s.relaxSubtitle
        TechniqueCategory.FOCUS -> s.focusSubtitle
        TechniqueCategory.NADI_SHODHAN -> s.nadiSubtitle
        TechniqueCategory.CHAKRAS -> s.chakrasSubtitle
        TechniqueCategory.MANTRA -> s.mantraSubtitle
    }
}

@Composable
fun BreathingTechnique.localizedName(): String {
    val s = S.current
    return when (id) {
        "box" -> s.boxBreathingName
        "478" -> s.breathing478Name
        "bhramari" -> s.bhramariName
        "dirga" -> s.dirgaName
        "ujjayi" -> s.ujjayiName
        "slow" -> s.slowBreathingName
        "anulom" -> s.anulomVilomName
        "muladhara" -> s.muladharaName
        "svadhisthana" -> s.svadhisthanaName
        "manipura" -> s.manipuraName
        "anahata" -> s.anahataName
        "vishuddha" -> s.vishuddhaName
        "ajna" -> s.ajnaName
        "sahasrara" -> s.sahasraraName
        "all_chakras" -> s.allChakrasName
        "music_01" -> s.music174hzName
        "music_02" -> s.music285hzName
        "music_03" -> s.music396hzName
        "music_04" -> s.music417hzName
        "music_05" -> s.music432hzName
        "music_06" -> s.music528hzName
        "music_07" -> s.music639hzName
        "music_08" -> s.music741hzName
        "music_09" -> s.music852hzName
        "music_10" -> s.music963hzName
        else -> name
    }
}

@Composable
fun BreathingTechnique.localizedSubtitle(): String {
    val s = S.current
    return when (id) {
        "box" -> s.equalRhythm
        "478" -> s.deepRelaxation
        "bhramari" -> s.beeBreath
        "dirga" -> s.threePartBreath
        "ujjayi" -> s.oceanBreath
        "slow" -> s.deepInhaleExhale
        "anulom" -> s.alternateNostril
        "muladhara" -> s.rootSubtitle
        "svadhisthana" -> s.sacralSubtitle
        "manipura" -> s.solarSubtitle
        "anahata" -> s.heartSubtitle
        "vishuddha" -> s.throatSubtitle
        "ajna" -> s.thirdEyeSubtitle
        "sahasrara" -> s.crownSubtitle
        "all_chakras" -> s.allChakrasSubtitle
        "music_01" -> s.music174hzSubtitle
        "music_02" -> s.music285hzSubtitle
        "music_03" -> s.music396hzSubtitle
        "music_04" -> s.music417hzSubtitle
        "music_05" -> s.music432hzSubtitle
        "music_06" -> s.music528hzSubtitle
        "music_07" -> s.music639hzSubtitle
        "music_08" -> s.music741hzSubtitle
        "music_09" -> s.music852hzSubtitle
        "music_10" -> s.music963hzSubtitle
        else -> subtitle
    }
}

@Composable
fun com.pranaflow.model.AmbientTrack.localizedLabel(): String {
    val s = S.current
    return when (this) {
        com.pranaflow.model.AmbientTrack.TRACK_1 -> s.omChant
        com.pranaflow.model.AmbientTrack.TRACK_2 -> s.rainThunder
        com.pranaflow.model.AmbientTrack.TRACK_3 -> s.forestNight
        com.pranaflow.model.AmbientTrack.TRACK_4 -> s.singingBowls
        com.pranaflow.model.AmbientTrack.TRACK_5 -> s.flute
    }
}

@Composable
fun BreathingTechnique.localizedDescription(): String {
    val s = S.current
    return when (id) {
        "muladhara" -> s.rootDesc
        "svadhisthana" -> s.sacralDesc
        "manipura" -> s.solarDesc
        "anahata" -> s.heartDesc
        "vishuddha" -> s.throatDesc
        "ajna" -> s.thirdEyeDesc
        "sahasrara" -> s.crownDesc + "\n\n" + s.sahasraraNote
        "all_chakras" -> s.allChakrasDesc
        "music_01" -> s.music174hzDesc
        "music_02" -> s.music285hzDesc
        "music_03" -> s.music396hzDesc
        "music_04" -> s.music417hzDesc
        "music_05" -> s.music432hzDesc
        "music_06" -> s.music528hzDesc
        "music_07" -> s.music639hzDesc
        "music_08" -> s.music741hzDesc
        "music_09" -> s.music852hzDesc
        "music_10" -> s.music963hzDesc
        else -> chakraInfo?.description ?: ""
    }
}
