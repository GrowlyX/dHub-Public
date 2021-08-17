package com.solexgames.pear.module;

import com.solexgames.pear.module.impl.HubModuleItemAdapter;
import com.solexgames.pear.module.impl.HubModuleScoreboardAdapter;

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
