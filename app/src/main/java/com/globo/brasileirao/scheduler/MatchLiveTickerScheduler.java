package com.globo.brasileirao.scheduler;

public interface MatchLiveTickerScheduler {

    /**
     * Schedule match live ticker synchronization to be performed periodically using an interval in background.
     * Any subsequent calls to this method will update the interval and the match id of the existing schedule.
     * @param matchId match identification.
     * @param interval in seconds.
     */
    void syncMatchLiveTicker(int matchId, long interval);

    /**
     * Cancel match live ticker synchronization schedule.
     */
    void cancelMatchesLiveTickerSync();
}
