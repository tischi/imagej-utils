package de.embl.cba.tables.plot;

import de.embl.cba.tables.color.ColoringModel;
import de.embl.cba.tables.tablerow.TableRow;
import net.imglib2.KDTree;
import net.imglib2.RealPoint;
import net.imglib2.Sampler;
import net.imglib2.neighborsearch.RadiusNeighborSearchOnKDTree;
import net.imglib2.type.numeric.ARGBType;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class RealPointARGBTypeBiConsumerSupplier< T extends TableRow > implements Supplier< BiConsumer< RealPoint, ARGBType > >
{
	private final Supplier< KDTree< T > > kdTreeSupplier;
	private final ColoringModel< T > coloringModel;
	private final double radius;
	private int n = 0;

	public RealPointARGBTypeBiConsumerSupplier( Supplier< KDTree< T > > kdTreeSupplier, ColoringModel< T > coloringModel, final double radius )
	{
		this.kdTreeSupplier = kdTreeSupplier;
		this.coloringModel = coloringModel;
		this.radius = radius;
	}

	@Override
	public BiConsumer< RealPoint, ARGBType > get()
	{
		//System.out.println( ++n );
		return new RealPointARGBTypeBiConsumer( kdTreeSupplier.get(), coloringModel, radius );
	}

	class RealPointARGBTypeBiConsumer implements BiConsumer< RealPoint, ARGBType >
	{
		private final RadiusNeighborSearchOnKDTree< T > search;
		private final ColoringModel< T > coloringModel;
		private final double radius;

		public RealPointARGBTypeBiConsumer( KDTree< T > kdTree, ColoringModel< T > coloringModel, double radius )
		{
			search = new RadiusNeighborSearchOnKDTree<>( kdTree );
			this.coloringModel = coloringModel;
			this.radius = radius;
		}

		@Override
		public void accept( RealPoint realPoint, ARGBType argbType )
		{
			search.search( realPoint, radius, true );

			if ( search.numNeighbors() > 0 )
			{
				final Sampler< T > sampler = search.getSampler( 0 );
				final T tableRow = sampler.get();
				coloringModel.convert( tableRow, argbType );
			}
			else
			{
				argbType.setZero();
			}
		}
	}
}