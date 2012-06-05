package geogebra.gui;

import geogebra.common.euclidian.AbstractEuclidianView;
import geogebra.common.kernel.geos.FromMeta;
import geogebra.common.kernel.geos.GeoElement;
import geogebra.common.main.AbstractApplication;
import geogebra.euclidianND.EuclidianViewND;
import geogebra.main.Application;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.TreeSet;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuItem;


/**
 * Popup Menu for choosing a geo
 * 
 * @author mathieu
 *
 */
public class ContextMenuChooseGeo extends ContextMenuGeoElement {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	


	
	/**
	 * 
	 */
	protected AbstractEuclidianView view;
	
	/**
	 * polygons/polyhedra parents of segments, polygons, ...
	 */
	private TreeSet<GeoElement> metas;
	
	private ArrayList<GeoElement> selectedGeos;
	private ArrayList<GeoElement> geos;
	private geogebra.common.awt.Point loc;
	
	private JMenu selectAnotherMenu;

	/**
	 * 
	 * @param app application
	 * @param view view
	 * @param selectedGeos selected geos
	 * @param geos geos
	 * @param location place to show
	 */
	public ContextMenuChooseGeo(Application app, EuclidianViewND view, 
			ArrayList<GeoElement> selectedGeos,
			ArrayList<GeoElement> geos, Point location, geogebra.common.awt.Point invokerLocation) {

		super(app, selectedGeos, location);

		//return if just one geo, or if first geos more than one
		if (geos.size()<2 || selectedGeos.size()>1)
			return;
		
		//section to choose a geo
		//addSeparator();
		addSelectAnotherMenu();
		
		
		this.loc = invokerLocation;
		this.selectedGeos = selectedGeos;
		this.geos = geos;
		this.view = view;
		
		GeoElement geoSelected = selectedGeos.get(0);
		
		
		//add geos
		metas = new TreeSet<GeoElement>();
		
		for (GeoElement geo : geos){
			if (geo!=geoSelected){//don't add selected geo
				addGeo(geo);
			}
			
			if (geo.isFromMeta()){
				GeoElement meta = ((FromMeta) geo).getMeta();
				if (!metas.contains(meta)){
					addGeo(meta);
				}
			}
		}
		
		//TODO: clear selection is not working from here
		this.getSelectionModel().clearSelection();
		
	}
	
	
	
	private void addSelectAnotherMenu(){
		selectAnotherMenu = new JMenu(app.getPlain("SelectAnother") );
		selectAnotherMenu.setIcon(app.getEmptyIcon());
		selectAnotherMenu.setBackground(getBackground());
		selectAnotherMenu.setFont(app.getItalicFont());
		
		// add the selection menu just under the title
		add(selectAnotherMenu,1);     
		
	}
	
	
	/**
	 * 
	 */
	private void addGeo(GeoElement geo) {
		
		GeoAction chooser = new GeoAction(geo);
		JMenuItem mi = selectAnotherMenu.add(chooser);    
		mi.setBackground(bgColor);  
		mi.setText(getDescription(geo));			
		mi.addMouseListener(new MyMouseAdapter(geo));
	
		//prevent to add meta twice
		metas.add(geo);
			
		            
	}
	
	/**
	 * Action when select a geo
	 * @author mathieu
	 *
	 */
	private class GeoAction extends AbstractAction {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		private GeoElement geo;
		
		
		/**
		 * Create chooser for this geo
		 * @param geo geo to choose
		 */
		public GeoAction(GeoElement geo){
			super();
			this.geo = geo;
				
		}
		
		public void actionPerformed(ActionEvent e) {

			//AbstractApplication.debug(geo.getLabelSimple());
			app.clearSelectedGeos(false); //repaint done next step
			app.addSelectedGeo(geo);
			
			// update the geo lists and show the popup again with the new selection
			selectedGeos.clear();
			selectedGeos.add(geo);
			app.getGuiManager().showPopupChooseGeo(selectedGeos, geos, (EuclidianViewND)view, loc);
		}
		
	}
	
	
	private class MyMouseAdapter extends MouseAdapter{
		
		private GeoElement geo;
		
		public MyMouseAdapter(GeoElement geo){
			this.geo=geo;
		}
		
		@Override
		public void mouseEntered(MouseEvent e) {
			//AbstractApplication.debug(geo.getLabelSimple());
			/*
			geo.setHighlighted(true);
			app.getKernel().notifyRepaint();
			*/
			view.getEuclidianController().doSingleHighlighting(geo);
		}
		
		/*
		@Override
		public void mouseExited(MouseEvent e) {
			AbstractApplication.debug(geo.getLabelSimple());
			geo.setHighlighted(false);
			app.getKernel().notifyRepaint();
			
		}
		*/
	}
}
