package com.pranaflow.engine

import com.pranaflow.model.BreathPhase
import com.pranaflow.model.BreathState
import com.pranaflow.model.TimingConfig

/**
 * Stateless breathing engine.
 * Given elapsed time and a timing config, computes the current breath state.
 * This is a pure function with no side effects — all animation timing
 * is derived from the elapsed milliseconds.
 */
class BreathingEngine {

    /**
     * Compute the breath state at a given elapsed time.
     *
     * @param elapsedMs Total elapsed time since session start.
     * @param config    Timing configuration for the current technique.
     * @return Current [BreathState] including phase, progress, and cycle count.
     */
    fun computeState(elapsedMs: Long, config: TimingConfig): BreathState {
        if (config.cycleDurationMs <= 0) {
            return BreathState(BreathPhase.IDLE, 0f, 0)
        }

        val cycleMs = config.cycleDurationMs.toLong()
        val cycleCount = (elapsedMs / cycleMs).toInt()
        val positionInCycle = elapsedMs % cycleMs

        var accumulated = 0L
        for ((phase, durationMs) in config.phases) {
            val phaseEnd = accumulated + durationMs
            if (positionInCycle < phaseEnd) {
                val phaseElapsed = positionInCycle - accumulated
                val progress = (phaseElapsed.toFloat() / durationMs).coerceIn(0f, 1f)
                return BreathState(phase, progress, cycleCount)
            }
            accumulated = phaseEnd
        }

        // Fallback (shouldn't reach here)
        return BreathState(BreathPhase.INHALE, 0f, cycleCount)
    }

    /**
     * Apply ease-in-out smoothing to a linear progress value.
     * Produces the signature meditative feel — no abrupt starts or stops.
     */
    fun easeInOut(t: Float): Float {
        return if (t < 0.5f) {
            4f * t * t * t
        } else {
            1f - (-2f * t + 2f).let { it * it * it } / 2f
        }
    }
}
