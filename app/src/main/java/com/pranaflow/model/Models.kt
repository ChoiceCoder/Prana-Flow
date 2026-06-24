package com.pranaflow.model

enum class BreathPhase(val label: String) {
    INHALE("Inhale"),
    HOLD_IN("Hold"),
    EXHALE("Exhale"),
    HOLD_OUT("Hold"),
    IDLE("Ready")
}

enum class GeometryType {
    SQUARE_MANDALA,
    CIRCULAR_MANDALA,
    IDA_PINGALA,
    FLOWER_OF_LIFE,
    SRI_YANTRA,
    TRIPLE_LOTUS,
    CHAKRA
}

enum class TechniqueCategory(val label: String, val subtitle: String) {
    RELAX("Relax / Stress Relief", "Calm your nervous system"),
    FOCUS("Focus Boost", "Sharpen your awareness"),
    NADI_SHODHAN("Nadi Shodhan", "Purify energy channels"),
    CHAKRAS("7 Chakras", "Awaken your energy centers"),
    MANTRA("Music Healing", "Solfeggio frequencies for healing")
}

enum class AmbientTrack(val label: String, val resourceName: String) {
    TRACK_1("Om Chant", "ambient_01"),
    TRACK_2("Rain & Thunder", "ambient_02"),
    TRACK_3("Forest Night", "ambient_03"),
    TRACK_4("Singing Bowls", "ambient_04"),
    TRACK_5("Flute", "ambient_05")
}

data class TimingConfig(
    val inhaleMs: Int,
    val holdInMs: Int,
    val exhaleMs: Int,
    val holdOutMs: Int
) {
    val cycleDurationMs: Int get() = inhaleMs + holdInMs + exhaleMs + holdOutMs

    val phases: List<Pair<BreathPhase, Int>>
        get() = buildList {
            add(BreathPhase.INHALE to inhaleMs)
            if (holdInMs > 0) add(BreathPhase.HOLD_IN to holdInMs)
            add(BreathPhase.EXHALE to exhaleMs)
            if (holdOutMs > 0) add(BreathPhase.HOLD_OUT to holdOutMs)
        }
}

data class ChakraInfo(
    val mantra: String,
    val bodyY: Float,
    val bodyX: Float = 0.5f,
    val mantraResource: String = "",
    val colorHex: Long = 0xFFD4AF37,
    val description: String = ""
)

data class BreathingTechnique(
    val id: String,
    val name: String,
    val subtitle: String,
    val category: TechniqueCategory,
    val geometryType: GeometryType,
    val defaultTiming: TimingConfig,
    val durationLabel: String,
    val chakraInfo: ChakraInfo? = null
)

data class BreathState(
    val phase: BreathPhase,
    val phaseProgress: Float,
    val cycleCount: Int
)

object Techniques {

    // ── Relax ───────────────────────────────────────────────

    val boxBreathing = BreathingTechnique(
        id = "box", name = "4-4-4 Kumbhak",
        subtitle = "Equal rhythm, deep calm",
        category = TechniqueCategory.RELAX,
        geometryType = GeometryType.SQUARE_MANDALA,
        defaultTiming = TimingConfig(4000, 4000, 4000, 4000),
        durationLabel = "4-4-4-4"
    )

    val breathing478 = BreathingTechnique(
        id = "478", name = "4-7-8 Kumbhak",
        subtitle = "Deep relaxation technique",
        category = TechniqueCategory.RELAX,
        geometryType = GeometryType.CIRCULAR_MANDALA,
        defaultTiming = TimingConfig(4000, 7000, 8000, 0),
        durationLabel = "4-7-8"
    )

    val bhramari = BreathingTechnique(
        id = "bhramari", name = "Bhramari",
        subtitle = "Bee breath, heals anxiety",
        category = TechniqueCategory.RELAX,
        geometryType = GeometryType.FLOWER_OF_LIFE,
        defaultTiming = TimingConfig(5000, 0, 5000, 0),
        durationLabel = "5-5"
    )

    val dirga = BreathingTechnique(
        id = "dirga", name = "Dirga Pranayam",
        subtitle = "Three-part healing breath",
        category = TechniqueCategory.RELAX,
        geometryType = GeometryType.TRIPLE_LOTUS,
        defaultTiming = TimingConfig(6000, 3000, 6000, 3000),
        durationLabel = "6-3-6-3"
    )

    // ── Focus ───────────────────────────────────────────────

    val ujjayi = BreathingTechnique(
        id = "ujjayi", name = "Ujjayi",
        subtitle = "Ocean breath, inner fire",
        category = TechniqueCategory.FOCUS,
        geometryType = GeometryType.SRI_YANTRA,
        defaultTiming = TimingConfig(5000, 2000, 5000, 2000),
        durationLabel = "5-2-5-2"
    )

    val slowBreathing = BreathingTechnique(
        id = "slow", name = "Slow Breathing",
        subtitle = "Deep inhale, deep exhale",
        category = TechniqueCategory.FOCUS,
        geometryType = GeometryType.CIRCULAR_MANDALA,
        defaultTiming = TimingConfig(10000, 0, 10000, 0),
        durationLabel = "10-10"
    )

    // ── Nadi Shodhan ────────────────────────────────────────

    val anulomVilom = BreathingTechnique(
        id = "anulom", name = "Anulom Vilom",
        subtitle = "Alternate nostril balance",
        category = TechniqueCategory.NADI_SHODHAN,
        geometryType = GeometryType.IDA_PINGALA,
        defaultTiming = TimingConfig(5000, 0, 5000, 0),
        durationLabel = "5-5"
    )

    // ── 7 Chakras ───────────────────────────────────────────
    private val chakraTiming = TimingConfig(5000, 3000, 5000, 3000)

    val allChakras = BreathingTechnique(
        id = "all_chakras", name = "All 7 Chakras",
        subtitle = "Complete chakra healing",
        category = TechniqueCategory.CHAKRAS,
        geometryType = GeometryType.CHAKRA,
        defaultTiming = chakraTiming,
        durationLabel = "",
        chakraInfo = ChakraInfo(
            mantra = "卐",
            bodyY = 0.0f,
            mantraResource = "mantra_all_chakras",
            colorHex = 0xFFD4AF37,
            description = "Listen to this music to heal all your 7 chakras at once."
        )
    )

    val muladhara = BreathingTechnique(
        id = "muladhara", name = "Muladhara",
        subtitle = "Root · Grounding & stability",
        category = TechniqueCategory.CHAKRAS,
        geometryType = GeometryType.CHAKRA,
        defaultTiming = chakraTiming,
        durationLabel = "",
        chakraInfo = ChakraInfo(
            mantra = "लं", bodyY = 0.6272f,
            mantraResource = "mantra_muladhara",
            colorHex = 0xFFE53935,
            description = "Located at the base of the spine; it provides a sense of stability, security, and groundedness."
        )
    )

    val svadhisthana = BreathingTechnique(
        id = "svadhisthana", name = "Svadhisthana",
        subtitle = "Sacral · Creativity & flow",
        category = TechniqueCategory.CHAKRAS,
        geometryType = GeometryType.CHAKRA,
        defaultTiming = chakraTiming,
        durationLabel = "",
        chakraInfo = ChakraInfo(
            mantra = "वं", bodyY = 0.5809f,
            mantraResource = "mantra_svadhisthana",
            colorHex = 0xFFFB8C00,
            description = "Located in the lower abdomen; it governs your creativity, emotions, and sexual energy."
        )
    )

    val manipura = BreathingTechnique(
        id = "manipura", name = "Manipura",
        subtitle = "Solar Plexus · Power & will",
        category = TechniqueCategory.CHAKRAS,
        geometryType = GeometryType.CHAKRA,
        defaultTiming = chakraTiming,
        durationLabel = "",
        chakraInfo = ChakraInfo(
            mantra = "रं", bodyY = 0.5345f,
            mantraResource = "mantra_manipura",
            colorHex = 0xFFFDD835,
            description = "Located in the stomach area; it builds self-confidence, willpower, and personal power."
        )
    )

    val anahata = BreathingTechnique(
        id = "anahata", name = "Anahata",
        subtitle = "Heart · Love & compassion",
        category = TechniqueCategory.CHAKRAS,
        geometryType = GeometryType.CHAKRA,
        defaultTiming = chakraTiming,
        durationLabel = "",
        chakraInfo = ChakraInfo(
            mantra = "यं", bodyY = 0.4693f,
            mantraResource = "mantra_anahata",
            colorHex = 0xFF43A047,
            description = "Located in the center of the chest; it is the center for love, compassion, and emotional healing."
        )
    )

    val vishuddha = BreathingTechnique(
        id = "vishuddha", name = "Vishuddha",
        subtitle = "Throat · Expression & truth",
        category = TechniqueCategory.CHAKRAS,
        geometryType = GeometryType.CHAKRA,
        defaultTiming = chakraTiming,
        durationLabel = "",
        chakraInfo = ChakraInfo(
            mantra = "हं", bodyY = 0.4083f,
            mantraResource = "mantra_vishuddha",
            colorHex = 0xFF1E88E5,
            description = "Located in the throat; it helps with clear communication and expressing your truth."
        )
    )

    val ajna = BreathingTechnique(
        id = "ajna", name = "Ajna",
        subtitle = "Third Eye · Intuition & insight",
        category = TechniqueCategory.CHAKRAS,
        geometryType = GeometryType.CHAKRA,
        defaultTiming = chakraTiming,
        durationLabel = "",
        chakraInfo = ChakraInfo(
            mantra = "", bodyY = 0.3458f,
            mantraResource = "mantra_ajna",
            colorHex = 0xFF5C6BC0,
            description = "Located between the eyebrows; it enhances intuition, imagination, and mental focus."
        )
    )

    val sahasrara = BreathingTechnique(
        id = "sahasrara", name = "Sahasrara",
        subtitle = "Crown · Consciousness & bliss",
        category = TechniqueCategory.CHAKRAS,
        geometryType = GeometryType.CHAKRA,
        defaultTiming = chakraTiming,
        durationLabel = "",
        chakraInfo = ChakraInfo(
            mantra = "ॐ", bodyY = 0.3116f,
            mantraResource = "mantra_sahasrara",
            colorHex = 0xFFAB47BC,
            description = "Located at the top of the head; it connects you to spiritual consciousness and inner peace."
        )
    )

    // ── Music Healing (Solfeggio Frequencies) ───────────────
    // Audio files: res/raw/music_healing_01.mp3 through music_healing_10.mp3
    // To add more tracks in future: follow the same pattern below,
    // increment the id suffix, and add strings in AppStrings.kt

    private val musicTiming = TimingConfig(5000, 3000, 5000, 3000)

    val music174hz = BreathingTechnique(
        id = "music_01", name = "174Hz Pain Release",
        subtitle = "Foundation frequency · Reduces pain",
        category = TechniqueCategory.MANTRA,
        geometryType = GeometryType.CHAKRA,
        defaultTiming = musicTiming,
        durationLabel = "",
        chakraInfo = ChakraInfo(
            mantra = "174", bodyY = 0.0f,
            mantraResource = "music_healing_01",
            colorHex = 0xFF8B0000,
            description = "The lowest Solfeggio frequency. It acts as a natural anesthetic, reducing pain and giving a sense of safety and love."
        )
    )

    val music285hz = BreathingTechnique(
        id = "music_02", name = "285Hz Influence Energy Field",
        subtitle = "Healing frequency · Restores tissues",
        category = TechniqueCategory.MANTRA,
        geometryType = GeometryType.CHAKRA,
        defaultTiming = musicTiming,
        durationLabel = "",
        chakraInfo = ChakraInfo(
            mantra = "285", bodyY = 0.0f,
            mantraResource = "music_healing_02",
            colorHex = 0xFF4B0082,
            description = "Influences energy fields and helps heal cuts, burns and damaged tissues. Sends a message to restructure damaged organs and tissues."
        )
    )

    val music396hz = BreathingTechnique(
        id = "music_03", name = "396Hz Liberates Fear & Guilt",
        subtitle = "Liberation frequency · Releases fear",
        category = TechniqueCategory.MANTRA,
        geometryType = GeometryType.CHAKRA,
        defaultTiming = musicTiming,
        durationLabel = "",
        chakraInfo = ChakraInfo(
            mantra = "396", bodyY = 0.0f,
            mantraResource = "music_healing_03",
            colorHex = 0xFF800000,
            description = "Liberates you from feelings of fear and guilt. It cleanses feelings of guilt that often represent one of the basic obstacles to realization."
        )
    )

    val music417hz = BreathingTechnique(
        id = "music_04", name = "417Hz Facilitates Change",
        subtitle = "Change frequency · Clears negativity",
        category = TechniqueCategory.MANTRA,
        geometryType = GeometryType.CHAKRA,
        defaultTiming = musicTiming,
        durationLabel = "",
        chakraInfo = ChakraInfo(
            mantra = "417", bodyY = 0.0f,
            mantraResource = "music_healing_04",
            colorHex = 0xFFFF8C00,
            description = "Facilitates change and undoes situations. It clears traumatic experiences and cleanses destructive influences of past events."
        )
    )

    val music432hz = BreathingTechnique(
        id = "music_05", name = "432Hz Miracle Tone Of Nature",
        subtitle = "Nature frequency · Deep harmony",
        category = TechniqueCategory.MANTRA,
        geometryType = GeometryType.CHAKRA,
        defaultTiming = musicTiming,
        durationLabel = "",
        chakraInfo = ChakraInfo(
            mantra = "432", bodyY = 0.0f,
            mantraResource = "music_healing_05",
            colorHex = 0xFF228B22,
            description = "Known as the miracle tone of nature, 432Hz fills the mind with a sense of peace and well-being. It aligns you with the heartbeat of the universe."
        )
    )

    val music528hz = BreathingTechnique(
        id = "music_06", name = "528Hz Repairs DNA",
        subtitle = "Love frequency · DNA repair",
        category = TechniqueCategory.MANTRA,
        geometryType = GeometryType.CHAKRA,
        defaultTiming = musicTiming,
        durationLabel = "",
        chakraInfo = ChakraInfo(
            mantra = "528", bodyY = 0.0f,
            mantraResource = "music_healing_06",
            colorHex = 0xFF006400,
            description = "Known as the Love Frequency. It is central to the musical mathematical matrix of creation and is said to repair DNA and restore human consciousness to its original state."
        )
    )

    val music639hz = BreathingTechnique(
        id = "music_07", name = "639Hz Heals Relationships",
        subtitle = "Connection frequency · Harmonizes",
        category = TechniqueCategory.MANTRA,
        geometryType = GeometryType.CHAKRA,
        defaultTiming = musicTiming,
        durationLabel = "",
        chakraInfo = ChakraInfo(
            mantra = "639", bodyY = 0.0f,
            mantraResource = "music_healing_07",
            colorHex = 0xFF1565C0,
            description = "Enables creation of harmonious community and harmonious interpersonal relationships. It can be used for dealing with relationship problems."
        )
    )

    val music741hz = BreathingTechnique(
        id = "music_08", name = "741Hz Awaken Intuition",
        subtitle = "Intuition frequency · Cleanses cells",
        category = TechniqueCategory.MANTRA,
        geometryType = GeometryType.CHAKRA,
        defaultTiming = musicTiming,
        durationLabel = "",
        chakraInfo = ChakraInfo(
            mantra = "741", bodyY = 0.0f,
            mantraResource = "music_healing_08",
            colorHex = 0xFF0D47A1,
            description = "Leads to a purer, stable and spiritual life. It cleanses the cells from electromagnetic radiation and toxins. It awakens intuition and leads to a healthier, simpler life."
        )
    )

    val music852hz = BreathingTechnique(
        id = "music_09", name = "852Hz Attract Soul Tribe",
        subtitle = "Soul frequency · Awakens inner order",
        category = TechniqueCategory.MANTRA,
        geometryType = GeometryType.CHAKRA,
        defaultTiming = musicTiming,
        durationLabel = "",
        chakraInfo = ChakraInfo(
            mantra = "852", bodyY = 0.0f,
            mantraResource = "music_healing_09",
            colorHex = 0xFF6A0DAD,
            description = "Directly connected to the principle of Light. It enables you to see through illusions of life and awakens intuition to return to spiritual order and attract your soul tribe."
        )
    )

    val music963hz = BreathingTechnique(
        id = "music_10", name = "963Hz Connect With Light & Spirit",
        subtitle = "Divine frequency · Pure consciousness",
        category = TechniqueCategory.MANTRA,
        geometryType = GeometryType.CHAKRA,
        defaultTiming = musicTiming,
        durationLabel = "",
        chakraInfo = ChakraInfo(
            mantra = "963", bodyY = 0.0f,
            mantraResource = "music_healing_10",
            colorHex = 0xFFFFD700,
            description = "Associated with the Sahasrara chakra and awakening of perfect state. It connects you with the Light and all-embracing Spirit, enabling direct experience of the return to Oneness."
        )
    )

    val all = listOf(
        boxBreathing, breathing478, bhramari, dirga,
        ujjayi, slowBreathing,
        anulomVilom,
        allChakras, muladhara, svadhisthana, manipura, anahata, vishuddha, ajna, sahasrara,

    )

    val byCategory: List<Pair<TechniqueCategory, List<BreathingTechnique>>>
        get() = TechniqueCategory.entries
            .map { cat -> cat to all.filter { it.category == cat } }
            .filter { it.second.isNotEmpty() }

    fun findById(id: String): BreathingTechnique =
        all.first { it.id == id }
}
