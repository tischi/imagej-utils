import bdv.util.Bdv;
import bdv.util.BdvFunctions;
import bdv.util.BdvStackSource;
import de.embl.cba.bdv.utils.BdvUtils;
import de.embl.cba.bdv.utils.converters.argb.VolatileARGBConvertedRealSource;
import mpicbg.spim.data.SpimDataException;
import net.imglib2.realtransform.AffineTransform3D;
import net.imglib2.type.volatiles.VolatileARGBType;

public class TestMoveToPosition
{
	public static void main( String[] args ) throws SpimDataException, InterruptedException
	{
		final VolatileARGBConvertedRealSource labelsSource = ExampleARGBConvertedLabelsSource.getLabelsSource();

		final Bdv bdv = BdvFunctions.show( labelsSource ).getBdvHandle();

		// rotate somehow to check that 'moveToPosition' also works with rotated starting transform
		final AffineTransform3D transform3D = new AffineTransform3D();
		bdv.getBdvHandle().getViewerPanel().getState().getViewerTransform( transform3D );
		transform3D.translate( new double[]{200,30,30} );
		transform3D.rotate( 2,37 );
		transform3D.scale( 1.5 );
		transform3D.translate( new double[]{200,30,30} );

		bdv.getBdvHandle().getViewerPanel().setCurrentViewerTransform( transform3D );

		Thread.sleep( 1000 );

		BdvUtils.moveToPosition( bdv, new double[]{0,0,0}, 0,1000 );
	}
}
