package io.skydeck.gserver.enums;

import lombok.Getter;

public enum CardNameType {
    Slash(CardType.Basic, CardSubType.Slash, "slash"),
//    FireSlash(CardType.Basic, CardSubType.Slash, "fireSlash"),
//    ThunderSlash(CardType.Basic, CardSubType.Slash, "thunderSlash"),
//    IceSlash(CardType.Basic, CardSubType.Slash, "slash"),
    Cure(CardType.Basic, CardSubType.Cure, "cure"),
    Jink(CardType.Basic, CardSubType.Jink, "jink"),
    Liquor(CardType.Basic, CardSubType.Liquor, "liquor"),

    InvinciblePloy(CardType.Ploy, CardSubType.InstantPloy, "invincible"),
    ThrivePloy(CardType.Ploy, CardSubType.InstantPloy, "thrive"),
    MutualThrivePloy(CardType.Ploy, CardSubType.InstantPloy, "mutualThrive"),
    DismantlePloy(CardType.Ploy, CardSubType.InstantPloy, "dismantle"),
    GlancePloy(CardType.Ploy, CardSubType.InstantPloy, "glance"),
    StealPloy(CardType.Ploy, CardSubType.InstantPloy, "steal"),
    StealWeaponPloy(CardType.Ploy, CardSubType.InstantPloy, "stealWeapon"),
    FirePloy(CardType.Ploy, CardSubType.InstantPloy, "fire"),
    DuelPloy(CardType.Ploy, CardSubType.InstantPloy, "duel"),
    ChainPloy(CardType.Ploy, CardSubType.InstantPloy, "chain"),
    OptimizePloy(CardType.Ploy, CardSubType.InstantPloy, "optimize"),
    GroupThrivePloy(CardType.Ploy, CardSubType.InstantPloy, "groupThrive"),
    GroupCurePloy(CardType.Ploy, CardSubType.InstantPloy, "groupCure"),
    MassiveSlashPloy(CardType.Ploy, CardSubType.InstantPloy, "massiveSlash"),
    MassiveJinkPloy(CardType.Ploy, CardSubType.InstantPloy, "massiveJink"),
    SkipDrawPloy(CardType.Ploy, CardSubType.DelayPloy, "skipDraw"),
    SkipActivePloy(CardType.Ploy, CardSubType.DelayPloy, "skipActive"),
    LightningPloy(CardType.Ploy, CardSubType.DelayPloy, "lightning"),
    GroupExposePloy(CardType.Ploy, CardSubType.InstantPloy, "groupExpose"),
    AllianceCheerPloy(CardType.Ploy, CardSubType.InstantPloy, "allianceCheer"),
    MassiveChainPloy(CardType.Ploy, CardSubType.InstantPloy, "massiveChain"),
    SendLimboPloy(CardType.Ploy, CardSubType.InstantPloy, "sendLimbo"),
    GainStagePloy(CardType.Ploy, CardSubType.InstantPloy, "gainStage"),
    MassiveFirePloy(CardType.Ploy, CardSubType.InstantPloy, "massiveFire"),
    EnhancedDismantlePloy(CardType.Ploy, CardSubType.InstantPloy, "enhancedDismantle"),

    Zhugeliannu(CardType.Gear, CardSubType.Weapon, "zhugeliannu"),
    Feilongduofeng(CardType.Gear, CardSubType.Weapon, "feilongduofeng"),
    Qinggangjian(CardType.Gear, CardSubType.Weapon, "qinggangjian"),
    Hanbingjian(CardType.Gear, CardSubType.Weapon, "hanbingjian"),
    Guanshifu(CardType.Gear, CardSubType.Weapon, "guanshifu"),
    Zhangbashemao(CardType.Gear, CardSubType.Weapon, "zhangbashemao"),
    Qinglongyanyuedao(CardType.Gear, CardSubType.Weapon, "qinglongyanyuedao"),
    Sanjianliangrendao(CardType.Gear, CardSubType.Weapon, "sanjianliangrendao"),
    Zhuqueyushan(CardType.Gear, CardSubType.Weapon, "zhuqueshan"),
    Fangtianhuaji(CardType.Gear, CardSubType.Weapon, "fangtianhuaji"),
    Qilingong(CardType.Gear, CardSubType.Weapon, "qilingong"),

    Tengjia(CardType.Gear, CardSubType.Armor, "tengjia"),
    Baiyinshizi(CardType.Gear, CardSubType.Armor, "baiyinshizi"),
    Renwangdun(CardType.Gear, CardSubType.Armor, "renwangdun"),
    Baguazhen(CardType.Gear, CardSubType.Armor, "baguazhen"),
    Huxinjing(CardType.Gear, CardSubType.Armor, "huxinjing"),
    Mingguangkai(CardType.Gear, CardSubType.Armor, "mingguangkai"),
    Taipingyaoshu(CardType.Gear, CardSubType.Armor, "taipingyaoshu"),

    Jueying(CardType.Gear, CardSubType.DefenseRide, "jueying"),
    Dilu(CardType.Gear, CardSubType.DefenseRide, "dilu"),
    Chitu(CardType.Gear, CardSubType.OffenseRide, "chitu"),
    Zixing(CardType.Gear, CardSubType.OffenseRide, "zixing"),
    Dawan(CardType.Gear, CardSubType.OffenseRide, "dawan"),
    Liulongcanjia(CardType.Gear, CardSubType.SpecialRide, "liulongcanjia"),

    Muniuliuma(CardType.Gear, CardSubType.Treasure, "muniuliuma"),
    Yuxi(CardType.Gear, CardSubType.Treasure, "yuxi"),
    Dinglanyemingzhu(CardType.Gear, CardSubType.Treasure, "dinglanyemingzhu");

    @Getter
    private CardType cardType;
    @Getter
    private CardSubType cardSubType;
    @Getter
    private String simple;

    private CardNameType(CardType cardType, CardSubType cardSubType, String simple) {
        this.cardType = cardType;
        this.cardSubType = cardSubType;
        this.simple = simple;
    }

}
