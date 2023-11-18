package com.tigres810.usclb.common.data;

import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum ClipboardOrientation implements StringRepresentable
{
    DOWN_NORTH  (true, Direction.NORTH),
    DOWN_EAST   (true, Direction.EAST),
    DOWN_SOUTH  (true, Direction.SOUTH),
    DOWN_WEST   (true, Direction.WEST),
    NORTH       (false, Direction.NORTH),
    EAST        (false, Direction.EAST),
    SOUTH       (false, Direction.SOUTH),
    WEST        (false, Direction.WEST);

    private final String name = toString().toLowerCase(Locale.ROOT);
    private final boolean floor;
    private final Direction orientation;

    ClipboardOrientation(boolean floor, Direction orientation)
    {
        this.floor = floor;
        this.orientation = orientation;
    }

    public boolean isFloor()
    {
        return floor;
    }

    public Direction getOrientation()
    {
        return orientation;
    }

    public float toYRot()
    {
        return orientation.toYRot();
    }

    @Override
    public String getSerializedName()
    {
        return name;
    }



    public static ClipboardOrientation fromDirections(Direction face, Direction playerFacing)
    {
        return switch (face)
        {
            case NORTH -> NORTH;
            case EAST -> EAST;
            case SOUTH -> SOUTH;
            case WEST -> WEST;
            case DOWN -> null;
            case UP -> switch (playerFacing)
            {
                case NORTH -> DOWN_NORTH;
                case EAST -> DOWN_EAST;
                case SOUTH -> DOWN_SOUTH;
                case WEST -> DOWN_WEST;
                default -> throw new IllegalArgumentException("Player facing cannot be vertical");
            };
        };
    }
}