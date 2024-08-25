package io.skydeck.gserver.domain.player;

import io.skydeck.gserver.annotation.DValue;
import io.skydeck.gserver.domain.BaseStageState;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PlayerStageState extends BaseStageState {
    @DValue("false")
    private Boolean healthLocked;
    @DValue("false")
    private Boolean inLimbo;
    @DValue("1")
    private Integer slashQuota;
    @DValue("2")
    private Integer drawQuota;
    @DValue("0")
    private Integer extraSlashTarget;
    @DValue("1")
    private Integer liquorQuota;
    @DValue("0")
    private Integer extraHandQuota;
    @DValue("false")
    private Boolean gainCardFromDeck;
    @DValue("false")
    private Boolean causeInDanger;
    @DValue("0")
    private Integer lostCardCount;
    @DValue("0")
    private Integer killCount;
    @DValue("0")
    private Integer useCardCount;
    @DValue("0")
    private Integer reframeCardCount;
    @DValue("0")
    private Integer sacrificeCardCount;
    @DValue("0")
    private Integer discardCardCount;
    @DValue("0")
    private Integer useSlashCount;
    @DValue("0")
    private Integer useLiquorCount;
    @DValue("false")
    private Boolean drunk;
    @DValue("0")
    private Integer damageTakenCount;
    @DValue("0")
    private Integer damageDealtCount;
    @DValue("false")
    private Boolean primaryHeroSilent;
    @DValue("false")
    private Boolean viceHeroSilent;
    @DValue("false")
    private Boolean playerSilent;
    @DValue("")
    private String cardDisabled;
    @DValue("false")
    private Boolean recoverDisabled;
    @DValue("0")
    private Integer handGiftCount;
    @DValue("false")
    private Boolean skipNextDrawPhase;
    @DValue("false")
    private Boolean skipNextActivePhase;

    static {
        buildFieldMap(PlayerStageState.class);
    }
}
