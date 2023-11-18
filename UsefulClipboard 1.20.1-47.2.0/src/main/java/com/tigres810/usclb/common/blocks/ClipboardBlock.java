package com.tigres810.usclb.common.blocks;

import java.util.stream.Stream;

import com.tigres810.usclb.common.blockentitys.EntityClipboardBlock;
import com.tigres810.usclb.common.data.ClipboardOrientation;
import com.tigres810.usclb.core.init.BlockEntityInit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ClipboardBlock extends Block implements EntityBlock {

	public static final EnumProperty< ClipboardOrientation > PROPERTY = EnumProperty.create( "facing",
			ClipboardOrientation.class );
	private static final VoxelShape SHAPE_DN = Stream.of(
					Block.box( 6.300000000000001, 0.15, 12.1, 9.7, 0.19999999999999998, 12.5 ),
					Block.box( 6.65, 0, 12.49, 9.35, 0.2, 12.9 ),
					Block.box( 5.949999999999999, 0.09999999999999999, 11.64, 10.05, 0.20000000000000007, 12.110000000000001 ),
					Block.box( 5.1, 0, 12.1, 10.9, 0.15, 12.5 ),
					Block.box( 4.7, 0, 3.8999999999999986, 5.1000000000000005, 0.15, 12.099999999999998 ),
					Block.box( 5.1, 0, 3.5, 10.9, 0.15, 3.9000000000000004 ),
					Block.box( 10.9, 0, 3.8999999999999986, 11.3, 0.15, 12.099999999999998 ),
					Block.box( 5.1, 0, 3.9000000000000004, 10.9, 0.1, 12.1 ) )
			.reduce( ( v1, v2 ) -> Shapes.join( v1, v2, BooleanOp.OR ) ).get();
	private static final VoxelShape SHAPE_DS = Stream.of(
					Block.box( 6.400000000000002, 0.15, 3.5, 9.8, 0.19999999999999998, 3.9000000000000004 ),
					Block.box( 6.750000000000002, 0, 3.0999999999999996, 9.450000000000001, 0.2, 3.51 ),
					Block.box( 6.050000000000001, 0.09999999999999999, 3.889999999999999, 10.150000000000002,
							0.20000000000000007, 4.359999999999999 ),
					Block.box( 5.200000000000001, 0, 3.5, 11.000000000000002, 0.15, 3.9000000000000004 ),
					Block.box( 11, 0, 3.900000000000002, 11.400000000000002, 0.15, 12.100000000000001 ),
					Block.box( 5.200000000000001, 0, 12.1, 11.000000000000002, 0.15, 12.5 ),
					Block.box( 4.800000000000001, 0, 3.900000000000002, 5.200000000000001, 0.15, 12.100000000000001 ),
					Block.box( 5.200000000000001, 0, 3.9000000000000004, 11.000000000000002, 0.1, 12.1 ) )
			.reduce( ( v1, v2 ) -> Shapes.join( v1, v2, BooleanOp.OR ) ).get();
	private static final VoxelShape SHAPE_DE = Stream.of(
					Block.box( 3.5500000000000007, 0.15, 6.25, 3.950000000000001, 0.19999999999999998, 9.649999999999999 ),
					Block.box( 3.1500000000000004, 0, 6.6, 3.5600000000000005, 0.2, 9.299999999999999 ),
					Block.box( 3.9399999999999995, 0.09999999999999999, 5.899999999999999, 4.41, 0.20000000000000007, 10 ),
					Block.box( 3.5500000000000007, 0, 5.049999999999999, 3.950000000000001, 0.15, 10.85 ),
					Block.box( 3.950000000000003, 0, 4.6499999999999995, 12.150000000000002, 0.15, 5.05 ),
					Block.box( 12.15, 0, 5.049999999999999, 12.55, 0.15, 10.85 ),
					Block.box( 3.950000000000003, 0, 10.85, 12.150000000000002, 0.15, 11.25 ),
					Block.box( 3.950000000000001, 0, 5.049999999999999, 12.15, 0.1, 10.85 ) )
			.reduce( ( v1, v2 ) -> Shapes.join( v1, v2, BooleanOp.OR ) ).get();
	private static final VoxelShape SHAPE_DW = Stream.of(
					Block.box( 12.15, 0.15, 6.350000000000001, 12.55, 0.19999999999999998, 9.75 ),
					Block.box( 12.540000000000001, 0, 6.700000000000001, 12.950000000000001, 0.2, 9.4 ),
					Block.box( 11.690000000000001, 0.09999999999999999, 6, 12.160000000000002, 0.20000000000000007,
							10.100000000000001 ), Block.box( 12.15, 0, 5.15, 12.55, 0.15, 10.950000000000001 ),
					Block.box( 3.9499999999999993, 0, 10.95, 12.149999999999999, 0.15, 11.350000000000001 ),
					Block.box( 3.5500000000000007, 0, 5.15, 3.950000000000001, 0.15, 10.950000000000001 ),
					Block.box( 3.9499999999999993, 0, 4.75, 12.149999999999999, 0.15, 5.15 ),
					Block.box( 3.950000000000001, 0, 5.15, 12.15, 0.1, 10.950000000000001 ) )
			.reduce( ( v1, v2 ) -> Shapes.join( v1, v2, BooleanOp.OR ) ).get();
	private static final VoxelShape SHAPE_N = Stream.of( Block.box( 6.300000000000001, 12.1, 15.8, 9.7, 12.5, 15.85 ),
					Block.box( 6.65, 12.49, 15.8, 9.35, 12.9, 16 ),
					Block.box( 5.949999999999999, 11.64, 15.8, 10.05, 12.110000000000001, 15.9 ),
					Block.box( 5.1, 12.1, 15.85, 10.9, 12.5, 16 ),
					Block.box( 4.7, 3.8999999999999986, 15.85, 5.1000000000000005, 12.099999999999998, 16 ),
					Block.box( 5.1, 3.5, 15.85, 10.9, 3.9000000000000004, 16 ),
					Block.box( 10.9, 3.8999999999999986, 15.85, 11.3, 12.099999999999998, 16 ),
					Block.box( 5.1, 3.9000000000000004, 15.9, 10.9, 12.1, 16 ) )
			.reduce( ( v1, v2 ) -> Shapes.join( v1, v2, BooleanOp.OR ) ).get();
	private static final VoxelShape SHAPE_S = Stream.of(
					Block.box( 6.300000000000001, 12.1, 0.15000000000000036, 9.7, 12.5, 0.1999999999999993 ),
					Block.box( 6.65, 12.49, 0, 9.35, 12.9, 0.1999999999999993 ),
					Block.box( 5.949999999999999, 11.64, 0.09999999999999964, 10.05, 12.110000000000001, 0.1999999999999993 ),
					Block.box( 5.1, 12.1, 0, 10.9, 12.5, 0.15000000000000036 ),
					Block.box( 4.7, 3.8999999999999986, 0, 5.1000000000000005, 12.099999999999998, 0.15000000000000036 ),
					Block.box( 5.1, 3.5, 0, 10.9, 3.9000000000000004, 0.15000000000000036 ),
					Block.box( 10.9, 3.8999999999999986, 0, 11.3, 12.099999999999998, 0.15000000000000036 ),
					Block.box( 5.1, 3.9000000000000004, 0, 10.9, 12.1, 0.09999999999999964 ) )
			.reduce( ( v1, v2 ) -> Shapes.join( v1, v2, BooleanOp.OR ) ).get();
	private static final VoxelShape SHAPE_E = Stream.of(
					Block.box( 0.14999999999999858, 12.1, 6.300000000000001, 0.1999999999999993, 12.5, 9.7 ),
					Block.box( 0, 12.49, 6.65, 0.1999999999999993, 12.9, 9.35 ),
					Block.box( 0.10000000000000142, 11.64, 5.949999999999999, 0.1999999999999993, 12.110000000000001, 10.05 ),
					Block.box( 0, 12.1, 5.1, 0.14999999999999858, 12.5, 10.9 ),
					Block.box( 0, 3.8999999999999986, 4.699999999999999, 0.14999999999999858, 12.099999999999998,
							5.100000000000001 ), Block.box( 0, 3.5, 5.1, 0.14999999999999858, 3.9000000000000004, 10.9 ),
					Block.box( 0, 3.8999999999999986, 10.9, 0.14999999999999858, 12.099999999999998, 11.3 ),
					Block.box( 0, 3.9000000000000004, 5.1, 0.10000000000000142, 12.1, 10.9 ) )
			.reduce( ( v1, v2 ) -> Shapes.join( v1, v2, BooleanOp.OR ) ).get();
	private static final VoxelShape SHAPE_W = Stream.of(
					Block.box( 15.8, 12.1, 6.300000000000001, 15.850000000000001, 12.5, 9.7 ),
					Block.box( 15.8, 12.49, 6.65, 16, 12.9, 9.35 ),
					Block.box( 15.8, 11.64, 5.949999999999999, 15.899999999999999, 12.110000000000001, 10.05 ),
					Block.box( 15.850000000000001, 12.1, 5.1, 16, 12.5, 10.9 ),
					Block.box( 15.850000000000001, 3.8999999999999986, 4.699999999999999, 16, 12.099999999999998,
							5.100000000000001 ), Block.box( 15.850000000000001, 3.5, 5.1, 16, 3.9000000000000004, 10.9 ),
					Block.box( 15.850000000000001, 3.8999999999999986, 10.9, 16, 12.099999999999998, 11.3 ),
					Block.box( 15.899999999999999, 3.9000000000000004, 5.1, 16, 12.1, 10.9 ) )
			.reduce( ( v1, v2 ) -> Shapes.join( v1, v2, BooleanOp.OR ) ).get();

	public static final float BTN_MIN_Y = 4.125F / 16F;
	public static final float BTN_MAX_Y = 4.625F / 16F;
	public static final float BTN_LEFT_MIN_X = 5.45F / 16F;
	public static final float BTN_LEFT_MAX_X = 5.95F / 16F;
	public static final float BTN_RIGHT_MIN_X = 10.05F / 16F;
	public static final float BTN_RIGHT_MAX_X = 10.55F / 16F;

	public ClipboardBlock ( Properties pProperties ) {
		super( pProperties );
	}

	@Override
	public BlockEntity newBlockEntity ( BlockPos pPos, BlockState pState ) {
		return BlockEntityInit.CLIPBOARD_BLOCK_TILE.get().create( pPos, pState );
	}

	@Override
	public VoxelShape getShape ( BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext ) {

		return switch ( pState.getValue( PROPERTY ) ) {
			case DOWN_NORTH -> SHAPE_DN;
			case DOWN_EAST -> SHAPE_DE;
			case DOWN_WEST -> SHAPE_DW;
			case DOWN_SOUTH -> SHAPE_DS;
			case EAST -> SHAPE_E;
			case WEST -> SHAPE_W;
			case NORTH -> SHAPE_N;
			case SOUTH -> SHAPE_S;
		};
	}

	@Override
	public BlockState getStateForPlacement ( BlockPlaceContext context ) {
		Direction face = context.getClickedFace();
		Direction facing = context.getHorizontalDirection().getOpposite();
		ClipboardOrientation state = ClipboardOrientation.fromDirections( face, facing );

		if ( state != null ) {
			return this.defaultBlockState().setValue( PROPERTY, ClipboardOrientation.fromDirections( face, facing ) );
		} else {
			return null;
		}
	}

	@Override
	public InteractionResult use ( BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand,
			BlockHitResult hit ) {

		if ( !level.isClientSide && level.getBlockEntity( pos ) instanceof EntityClipboardBlock be ) {

			if ( isHoveringButton( pos, state, hit, false ) ) {
				player.level().playSound( null, player.blockPosition(), SoundEvents.STONE_BUTTON_CLICK_ON,
						SoundSource.PLAYERS, 1f, 1f );
				be.switchToPreviousPage();
			} else if ( isHoveringButton( pos, state, hit, true ) ) {
				player.level().playSound( null, player.blockPosition(), SoundEvents.STONE_BUTTON_CLICK_ON,
						SoundSource.PLAYERS, 1f, 1f );
				be.switchToNextPage();
			}
		}
		return InteractionResult.sidedSuccess( level.isClientSide );
	}

	@Override
	public void playerWillDestroy ( Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer ) {
		super.playerWillDestroy( pLevel, pPos, pState, pPlayer );
	}

	@Override
	protected void createBlockStateDefinition ( Builder< Block, BlockState > builder ) {
		builder.add( PROPERTY );
	}

	public static boolean isHoveringButton ( BlockPos pos, BlockState state, HitResult hit, boolean right ) {

		if ( hit instanceof BlockHitResult blockHit && blockHit.getType() != HitResult.Type.MISS ) {

			if ( !blockHit.getBlockPos().equals( pos ) ) {
				return false;
			}

			ClipboardOrientation orientation = state.getValue( PROPERTY );
			Direction dir = orientation.getOrientation();
			boolean xAxis = dir.getAxis() == Direction.Axis.X;
			Vec3 hitPos = hit.getLocation();

			Direction face = orientation.isFloor() ? Direction.UP : dir;

			if ( blockHit.getDirection() != face ) {
				return false;
			}

			double y;

			if ( orientation.isFloor() ) {
				y = xAxis ? hitPos.x : hitPos.z;
				y -= Math.floor( y );

				if ( dir.getAxisDirection() == Direction.AxisDirection.POSITIVE ) {
					y = 1D - y;
				}
			} else {
				y = hitPos.y - Math.floor( hitPos.y );
			}

			if ( y < BTN_MIN_Y || y > BTN_MAX_Y ) {
				return false;
			}

			double xz = xAxis ? hitPos.z : hitPos.x;
			xz -= Math.floor( xz );

			if ( dir.getClockWise().getAxisDirection() == Direction.AxisDirection.POSITIVE ) {
				xz = 1D - xz;
			}

			if ( right ) {
				return xz >= BTN_RIGHT_MIN_X && xz <= BTN_RIGHT_MAX_X;
			} else {
				return xz >= BTN_LEFT_MIN_X && xz <= BTN_LEFT_MAX_X;
			}
		}
		return false;
	}
}