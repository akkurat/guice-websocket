export interface IGame {
    players: string[]
    uuid: string
    state: GameState
    gameInfo: string
}

export enum GameState {
    NEW,
    STARTED,
    INTERRUPTED,
    FINISHED
}

export interface ICard {
    value: string
    color: 'KREUZ' | 'HERZ' | 'ECKEN' | 'SCHAUFEL'
}