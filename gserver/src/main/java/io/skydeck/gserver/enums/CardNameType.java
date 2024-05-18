package io.skydeck.gserver.enums;

import lombok.Getter;

public enum CardNameType {
    Slash(CardType.Basic, CardSubType.Slash),
    FireSlash(CardType.Basic, CardSubType.Slash),
    ThunderSlash(CardType.Basic, CardSubType.Slash),
    IceSlash(CardType.Basic, CardSubType.Slash),
    Cure(CardType.Basic, CardSubType.Cure),
    Jink(CardType.Basic, CardSubType.Jink),
    Liquor(CardType.Basic, CardSubType.Liquor),

    InvinciblePloy(CardType.Ploy, CardSubType.InstantPloy),
    ThrivePloy(CardType.Ploy, CardSubType.InstantPloy),
    MutualThrivePloy(CardType.Ploy, CardSubType.InstantPloy),
    DismantlePloy(CardType.Ploy, CardSubType.InstantPloy),
    GlancePloy(CardType.Ploy, CardSubType.InstantPloy),
    StealPloy(CardType.Ploy, CardSubType.InstantPloy),
    StealWeaponPloy(CardType.Ploy, CardSubType.InstantPloy),
    FirePloy(CardType.Ploy, CardSubType.InstantPloy),
    DuelPloy(CardType.Ploy, CardSubType.InstantPloy),
    ChainPloy(CardType.Ploy, CardSubType.InstantPloy),
    OptimizePloy(CardType.Ploy, CardSubType.InstantPloy),
    GroupThrivePloy(CardType.Ploy, CardSubType.InstantPloy),
    GroupCurePloy(CardType.Ploy, CardSubType.InstantPloy),
    MassiveSlashPloy(CardType.Ploy, CardSubType.InstantPloy),
    MassiveJinkPloy(CardType.Ploy, CardSubType.InstantPloy),
    SkipDrawPloy(CardType.Ploy, CardSubType.DelayPloy),
    SkipActivePloy(CardType.Ploy, CardSubType.DelayPloy),
    LightningPloy(CardType.Ploy, CardSubType.DelayPloy),
    GroupExposePloy(CardType.Ploy, CardSubType.InstantPloy),
    AllianceCheerPloy(CardType.Ploy, CardSubType.InstantPloy),
    MassiveChainPloy(CardType.Ploy, CardSubType.InstantPloy),
    SendLimboPloy(CardType.Ploy, CardSubType.InstantPloy),
    GainStagePloy(CardType.Ploy, CardSubType.InstantPloy),
    MassiveFirePloy(CardType.Ploy, CardSubType.InstantPloy),
    EnhancedDismantlePloy(CardType.Ploy, CardSubType.InstantPloy),

    Zhugeliannu(CardType.Gear, CardSubType.Weapon),
    Feilongduofeng(CardType.Gear, CardSubType.Weapon),
    Qinggangjian(CardType.Gear, CardSubType.Weapon),
    Hanbingjian(CardType.Gear, CardSubType.Weapon),
    Guanshifu(CardType.Gear, CardSubType.Weapon),
    Sanjianlianren(CardType.Gear, CardSubType.Weapon),
    Zhangbashemao(CardType.Gear, CardSubType.Weapon),
    Qinglongyanyuedao(CardType.Gear, CardSubType.Weapon),
    Zhuqueyushan(CardType.Gear, CardSubType.Weapon),
    Fangtianhuaji(CardType.Gear, CardSubType.Weapon),
    Qilingong(CardType.Gear, CardSubType.Weapon),

    Tengjia(CardType.Gear, CardSubType.Armor),
    Baiyinshizi(CardType.Gear, CardSubType.Armor),
    Renwangdun(CardType.Gear, CardSubType.Armor),
    Baguazhen(CardType.Gear, CardSubType.Armor),
    Huxinjing(CardType.Gear, CardSubType.Armor),
    Mingguangkai(CardType.Gear, CardSubType.Armor),
    Taipingyaoshu(CardType.Gear, CardSubType.Armor),

    Jueying(CardType.Gear, CardSubType.DefenseRide),
    Dilu(CardType.Gear, CardSubType.DefenseRide),
    Chitu(CardType.Gear, CardSubType.OffenseRide),
    Zixing(CardType.Gear, CardSubType.OffenseRide),
    Dawan(CardType.Gear, CardSubType.OffenseRide),
    Liulongcanjia(CardType.Gear, CardSubType.SpecialRide),

    Muniuliuma(CardType.Gear, CardSubType.Treasure),
    Yuxi(CardType.Gear, CardSubType.Treasure),
    Dinglanyemingzhu(CardType.Gear, CardSubType.Treasure);

    @Getter
    private CardType cardType;
    @Getter
    private CardSubType cardSubType;

    private CardNameType(CardType cardType, CardSubType cardSubType) {
        this.cardType = cardType;
        this.cardSubType = cardSubType;
    }

}
