package io.skydeck.gserver.domain.hero.shu;

import io.skydeck.gserver.domain.hero.StaticHeroBase;
import io.skydeck.gserver.enums.Gender;
import io.skydeck.gserver.enums.Kingdom;

import java.util.List;

public class SHeroGuanyu extends StaticHeroBase {
    @Override
    public String id() {
        return "SHU-002";
    }

    @Override
    public List<String> skills() {
        return List.of("WuSheng", "HuWei");
    }

    @Override
    public String name() {
        return "GuanYu";
    }

    @Override
    public String title() {
        return "WeiZhenHuaXia";
    }

    @Override
    public String maxHealth() {
        return "2.5";
    }

    @Override
    public Kingdom kingdom() {
        return Kingdom.Shu;
    }

    @Override
    public List<String> buddies() {
        return List.of("LiuBei", "ZhangFei");
    }

    @Override
    public Gender gender() {
        return Gender.Male;
    }
}
