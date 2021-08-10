package com.solexgames.hub.module;

import com.solexgames.hub.module.impl.HubModuleItemAdapter;
import com.solexgames.hub.module.impl.HubModuleScoreboardAdapter;

/**
 * @author GrowlyX
 * @since 8/5/2021
 */

public interface HubModule {

    default HubModuleItemAdapter getItemAdapter() {
        return null;
    }

    default HubModuleScoreboardAdapter getScoreboardAdapter() {
        return null;
    }

}
