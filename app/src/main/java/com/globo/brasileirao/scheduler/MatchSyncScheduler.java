package com.globo.brasileirao.scheduler;

public interface MatchSyncScheduler {

    /**
     * Schedule matches synchronization to be performed periodically using an interval in background.
     * Any subsequent calls to this method will update the interval of the existing schedule.
     * @param interval in seconds.
     */
    void syncMatches(long interval);

    /**
     * Cancel matches synchronization schedule.
     */
    void cancelMatchesSync();

}