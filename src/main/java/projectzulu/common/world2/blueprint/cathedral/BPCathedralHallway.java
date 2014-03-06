package projectzulu.common.world2.blueprint.cathedral;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.WeightedRandom;
import projectzulu.common.world.CellIndexDirection;
import projectzulu.common.world.dataobjects.BlockWithMeta;
import projectzulu.common.world2.BoundaryPair;
import projectzulu.common.world2.CellHelper;
import projectzulu.common.world2.blueprint.Blueprint;

public class BPCathedralHallway implements Blueprint {

    @Override
    public BlockWithMeta getBlockFromBlueprint(ChunkCoordinates piecePos, int cellSize, int cellHeight, Random random,
            CellIndexDirection cellIndexDirection) {
        piecePos = applyRotation(piecePos, cellSize, cellIndexDirection);
        piecePos = applyMirror(piecePos, cellSize, cellIndexDirection);

        return getWallBlock(piecePos, cellSize, cellHeight, random, cellIndexDirection);
    }

    private ChunkCoordinates applyRotation(ChunkCoordinates piecePos, int cellSize,
            CellIndexDirection cellIndexDirection) {
        return CellHelper.rotateCellTo(piecePos, cellSize, CellIndexDirection.WestWall);
    }

    private ChunkCoordinates applyMirror(ChunkCoordinates piecePos, int cellSize, CellIndexDirection cellIndexDirection) {
        if (cellIndexDirection == CellIndexDirection.WestWall) {
            piecePos = CellHelper.mirrorCellTo(piecePos, cellSize, CellIndexDirection.NorthWestCorner);
        } else if (cellIndexDirection == CellIndexDirection.EastWall) {
            return CellHelper.mirrorCellTo(piecePos, cellSize, CellIndexDirection.SouthWestCorner);
        }
        return piecePos;
    }

    public BlockWithMeta getWallBlock(ChunkCoordinates piecePos, int cellSize, int cellHeight, Random random,
            CellIndexDirection cellIndexDirection) {
        BlockWithMeta woodenPlank = new BlockWithMeta(5, 1);
        BlockWithMeta woodenStair = new BlockWithMeta(134);

        List<BlockWithMeta> wallBlocks = new ArrayList<BlockWithMeta>(3);
        wallBlocks.add(new BlockWithMeta(Block.stoneBrick.blockID, 2, 8)); // Cracked
        wallBlocks.add(new BlockWithMeta(Block.stoneBrick.blockID, 1, 8)); // Mossy
        wallBlocks.add(new BlockWithMeta(Block.stoneBrick.blockID, 0, 100)); // Regular

        /* Ceiling Top */
        if (piecePos.posY > cellHeight - cellSize) {
            int slope = CellHelper.getSlopeIndex(piecePos, cellSize - piecePos.posZ - 3, 1,
                    BoundaryPair.createPair(0, cellSize * 2 - 8), cellHeight - cellSize / 3);
            int slopeBelow = CellHelper.getSlopeIndex(piecePos, cellSize - piecePos.posZ - 2, 1,
                    BoundaryPair.createPair(0, cellSize * 2 - 8), cellHeight - cellSize / 3);
            if (slope == 0) {
                if (slope != slopeBelow) {
                    return new BlockWithMeta(woodenStair.blockID, getStairMeta(cellIndexDirection));
                } else {
                    return woodenPlank;
                }
            } else if (slope > 0) {
                return new BlockWithMeta("air");
            }
        }

        /* Outer Wall */
        if (piecePos.posZ == cellSize * 4 / 10) {
            //
            return (BlockWithMeta) WeightedRandom.getRandomItem(random, wallBlocks);
        }

        /* Mid Ceiling */
        int slope = CellHelper.getSlopeIndex(piecePos, cellSize - piecePos.posZ - 3, 1,
                BoundaryPair.createPair(1, cellSize * 2 - 8), cellHeight - cellSize);
        int slopeBelow = CellHelper.getSlopeIndex(piecePos, cellSize - piecePos.posZ - 2, 1,
                BoundaryPair.createPair(1, cellSize * 2 - 8), cellHeight - cellSize);
        if (slope == 0) {
            if (slope != slopeBelow) {
                return new BlockWithMeta(woodenStair.blockID, getStairMeta(cellIndexDirection));
            } else {
                return (BlockWithMeta) WeightedRandom.getRandomItem(random, wallBlocks);
            }
        }

        /* Upper Hallway */
        if (slope > 0 && piecePos.posZ > 2) {
            if ((piecePos.posX == 0 || piecePos.posX == cellSize - 1) && piecePos.posZ == 3) {
                return (BlockWithMeta) WeightedRandom.getRandomItem(random, wallBlocks);
            }

            if (piecePos.posZ == cellSize - 1 && piecePos.posX % 3 == 2 && slope == 1) {
                return new BlockWithMeta(woodenStair.blockID, getArchStairMeta(cellIndexDirection, false));
            }

            if (slope < 4 && piecePos.posZ == 3) {
                return new BlockWithMeta(Block.bookShelf.blockID);
            }
        }

        /* Arches */
        int topAarchSlope = CellHelper.getSlopeIndex(piecePos, cellSize - piecePos.posZ + 0, 1,
                BoundaryPair.createPair(1, cellSize * 2), cellHeight - cellSize);
        if (topAarchSlope == 0 && piecePos.posX % 3 == 1) {
            // only inside cathedral wall
            if (piecePos.posZ > cellSize * 4 / 10) {
                return new BlockWithMeta(woodenStair.blockID, getArchStairMeta(cellIndexDirection, true));
            }
        }

        int botAarchSlope = CellHelper.getSlopeIndex(piecePos, cellSize - piecePos.posZ + 1, 1,
                BoundaryPair.createPair(1, cellSize * 2), cellHeight - cellSize);
        if (botAarchSlope == 0 && piecePos.posX % 3 == 1) {
            // only inside cathedral wall
            if (piecePos.posZ > cellSize * 4 / 10) {
                return new BlockWithMeta(woodenStair.blockID, getArchStairMeta(cellIndexDirection, false));
            }
        }

        /* Pews */
        if (piecePos.posY == 1 && piecePos.posX % 2 == 1) {
            if (piecePos.posZ == cellSize * 4 / 10 + 2) {
                return new BlockWithMeta(Block.stairsWoodOak.blockID, getPewStairMeta(cellIndexDirection));
            } else if (piecePos.posZ > cellSize * 4 / 10 + 2) {
                return new BlockWithMeta(Block.woodSingleSlab.blockID);
            }
        }

        if (piecePos.posZ > cellSize * 4 / 10 && piecePos.posY == 0) {
            if (piecePos.posZ == cellSize * 4 / 10 + 1) {
                return new BlockWithMeta(Block.cobblestone.blockID);
            } else {
                return (BlockWithMeta) WeightedRandom.getRandomItem(random, wallBlocks);
            }
        }
        return new BlockWithMeta("air");
    }

    private int getStairMeta(CellIndexDirection cellIndexDirection) {
        switch (cellIndexDirection) {
        case WestWall:
            return 0;
        case EastWall:
            return 1;
        case SouthWall:
            return 3;
        case NorthWall:
        default:
            return 2;
        }
    }

    public int getArchStairMeta(CellIndexDirection cellIndexDirection, boolean top) {
        switch (cellIndexDirection) {
        case WestWall:
            return top ? 0 : 5;
        case EastWall:
            return top ? 1 : 4;
        default:
            return 2;
        }
    }

    private int getPewStairMeta(CellIndexDirection cellIndexDirection) {
        switch (cellIndexDirection) {
        case WestWall:
            return 1;
        case EastWall:
            return 0;
        default:
            return 0;
        }
    }

    @Override
    public String getIdentifier() {
        return "BPCathedralHallway";
    }

    @Override
    public int getWeight() {
        return 0;
    }
}