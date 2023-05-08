package org.deckmaster.mapgen;

public enum TileState {
    EMPTY(true),
    EVENT(true),

    LOOT(true),
    LOOT_EMPTY(true),

    TREE(true),
    PINE_TREE(true),
    FLOWER_TREE(true),
    FRUIT_TREE(true),

    MOSS_ROCK_1(true),
    MOSS_ROCK_2(true),
    MOSS_ROCK_3(false),

    ROCK_1(true),
    ROCK_2(true),
    ROCK_3(false),
    WHITE_ROCK_1(true),
    WHITE_ROCK_2(true),
    WHITE_ROCK_3(false),

    SNOW_ROCK_1(true),
    SNOW_ROCK_2(true),
    SNOW_ROCK_3(false),

    BUILDING_WALL(false),
    BUILDING_ENTRANCE(true),
    BUILDING_DRAW(false),
    BUILDING_INSIDE(true),
    BUILDING_EXIT(true),
    BUILDING_INSIDE_WALL(false),
    BUILDING_INSIDE_CHEST(true),
    BUILDING_INSIDE_CHEST_OPEN(true),
    BUILDING_INSIDE_EVENT(true);



    public final boolean walkable;
    TileState(boolean walkable) {
        this.walkable = walkable;
    }
}
