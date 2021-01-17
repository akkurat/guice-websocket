/* tslint:disable */
/* eslint-disable */
// Generated using typescript-generator version 2.28.785 on 2021-01-16 17:25:51.

interface UserMapEvent extends IServerMessage<UserPayload> {
    payload: UserPayload;
}

interface IllegalState extends IServerMessage<string> {
    to: PlayerReference;
    payload: string;
}

interface ModeEvent extends IServerMessage<ModePayload> {
    to: PlayerReference;
    payload: ModePayload;
}

interface Status extends IServerMessage<StatusPayload> {
    to: PlayerReference;
    payload: StatusPayload;
}

interface IPlayerReference {
    ref: string;
    team: ITeam;
}

interface UserPayload {
    playerList: IPlayerReference[];
    users: { [index: string]: string };
    youAre: string;
}

interface PlayerReference extends IPlayerReference {
    team: Team;
    proxy: Consumer<IServerMessage<any>>;
}

interface ModePayload {
    modes: { [index: string]: PresenterMode };
    cards: JassCard[];
}

interface StatusPayload {
    availCards: JassCard[];
    legalCards: JassCard[];
    roundCards: ImmutableLogEntry[];
    yourTurn: boolean;
    points: { [index: string]: number };
    mode: TrickMode;
    gameInfoPoints: ImmutableRound[];
}

interface ITeam {
    ref: string;
}

interface IServerMessage<T> extends IJassMessage<T> {
    to: IPlayerReference;
    buffertype: number;
}

interface Team extends ITeam {
}

interface Consumer<T> {
}

interface PresenterMode {
    description: string;
    modeFactory: IModeFactory;
}

interface JassCard {
    color: JassColor;
    value: JassValue;
}

interface ImmutableLogEntry {
    playerReference: IPlayerReference;
    card: JassCard;
    dateTime: Date;
}

interface TrickMode {
    caption: string;
    trickCaption: string;
    factor: number;
}

interface ImmutableRound {
    parmeterizedRound: IParmeterizedRound;
    pointsByPlayer: { [index: string]: number };
    pointsByTeam: { [index: string]: number };
    parametrizedTurns: ImmutableTrick[];
    lastRoundBonus: { [index: string]: number };
    totalPointsByTeam: { [index: string]: number };
}

interface IModeFactory {
}

interface IParmeterizedRound {
    semanticInfo: { [index: string]: any };
    factor: number;
    caption: string;
}

interface ImmutableTrick {
    log: ImmutableLogEntry[];
    whoTakes: IPlayerReference;
    sum: number;
    trickCaption: string;
}

interface IJassMessage<T> extends Serializable {
    payload: T;
    code: string;
}

interface Serializable {
}

type JassColor = "HERZ" | "ECKEN" | "SCHAUFEL" | "KREUZ";

type JassValue = "_6" | "_7" | "_8" | "_9" | "_10" | "_B" | "_D" | "_K" | "_A";
