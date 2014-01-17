package vm;

import java.util.List;

import model.Mahasiswa;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import service.MahasiswaService;

public class MahasiswaVM {
	
	@Wire("#listboxMahasiswa")
	private Listbox listboxMahasiswa;
	
	private Mahasiswa mahasiswa;
	private List<Mahasiswa> mahasiswas;
	@WireVariable
	private MahasiswaService mahasiswaService = (MahasiswaService) SpringUtil.getBean("mahasiswaService");

	public Mahasiswa getMahasiswa() {
		return mahasiswa;
	}

	public void setMahasiswa(Mahasiswa mahasiswa) {
		this.mahasiswa = mahasiswa;
	}

	public List<Mahasiswa> getMahasiswas() {
		return mahasiswas;
	}

	public void setMahasiswas(List<Mahasiswa> mahasiswas) {
		this.mahasiswas = mahasiswas;
	}

	public MahasiswaService getMahasiswaService() {
		return mahasiswaService;
	}

	public void setMahasiswaService(MahasiswaService mahasiswaService) {
		this.mahasiswaService = mahasiswaService;
	}
	
	private ListitemRenderer<Mahasiswa> listitemRendererMahasiswas;

	public ListitemRenderer<Mahasiswa> getListitemRendererMahasiswas() {
		return listitemRendererMahasiswas;
	}

	public void setListitemRendererMahasiswas(ListitemRenderer<Mahasiswa> listitemRendererMahasiswas) {
		this.listitemRendererMahasiswas = listitemRendererMahasiswas;
	}

	@AfterCompose
	public void initComponents(@ContextParam(ContextType.VIEW) Component component,
		@ExecutionArgParam("object") Object object,
		@ExecutionArgParam("mahasiswa") Mahasiswa mahasiswa) {

		Selectors.wireComponents(component, this, false);

		setMahasiswas(getMahasiswaService().getAllMahasiswas());
			
		if(mahasiswa == null){
				
			this.mahasiswa = new Mahasiswa();
				
			listitemRendererMahasiswas = doListitemRendererMahasiswa();
		}else {
				
			this.mahasiswa = mahasiswa;
		}
	}

	public ListitemRenderer<Mahasiswa> doListitemRendererMahasiswa(){
		return new ListitemRenderer<Mahasiswa>(){

			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public void render(Listitem listitem, Mahasiswa mahasiswaRender, int i)throws Exception {
				
				final Mahasiswa objectMahasiswa = mahasiswaRender;
				
				Listcell listcell;
				
				final Button buttonEdit = new Button();
					buttonEdit.setImage("/images/btn_edit2_16x16.gif");
					
		        final Button buttonSave = new Button();
		        	buttonSave.setImage("/images/btn_save2_16x16.gif");
		        
		        final Button buttonCancel = new Button();
		        	buttonCancel.setImage("/images/btn_cancel2_16x16.gif");
		        
		        final Button buttonDelete = new Button();
		        	buttonDelete.setImage("/images/btn_delete2_16x16.gif");
		        	
		        final Label labelNpm = new Label();	
		        final Label labelNama = new Label();
		        
		        final Textbox textboxNpm = new Textbox();
		        final Textbox textboxNama = new Textbox();

		        listcell = new Listcell();
		        	buttonEdit.setParent(listcell); 
		        	buttonSave.setParent(listcell); 
		        	buttonCancel.setParent(listcell); 
		        	buttonDelete.setParent(listcell); 
				listcell.setParent(listitem);
		        
				listcell = new Listcell();
					textboxNpm.setParent(listcell); 
					labelNpm.setParent(listcell); 
				listcell.setParent(listitem);

				listcell = new Listcell();
					textboxNama.setParent(listcell); 
					labelNama.setParent(listcell);
				listcell.setParent(listitem);

				if(mahasiswaRender.getNpm() == null){
					buttonEdit.setVisible(false);
					buttonDelete.setVisible(false);
					
				}else{
		        	buttonSave.setVisible(false);
		        	buttonCancel.setVisible(false);
		        	buttonDelete.setVisible(false);
		        	
		        	labelNpm.setValue(objectMahasiswa.getNpm());
		        	labelNama.setValue(objectMahasiswa.getNama());
		        	
		        	textboxNpm.setVisible(false);
		        	textboxNama.setVisible(false);
				}
				
				buttonSave.addEventListener(Events.ON_CLICK, new EventListener() {

					@Override
					public void onEvent(Event event) throws Exception {
						if(objectMahasiswa.getNpm() == null){
							System.out.println("Save");
							
							objectMahasiswa.setNpm(textboxNpm.getValue());
							objectMahasiswa.setNama(textboxNama.getValue());
							
							mahasiswaService.save(mahasiswa);
							
							BindUtils.postGlobalCommand(null, null, "refreshAfterSave", null);
						}else{
							System.out.println("Update");
							
							objectMahasiswa.setNama(textboxNama.getValue());
							
							mahasiswaService.update(mahasiswa);
							
							BindUtils.postGlobalCommand(null, null, "refreshAfterSave", null);
						}
					}
				});
				
				buttonEdit.addEventListener(Events.ON_CLICK, new EventListener() {

					@Override
					public void onEvent(Event event) throws Exception {
						
						buttonEdit.setVisible(false);
						buttonSave.setVisible(true);
						
						textboxNpm.setDisabled(true);
						textboxNpm.setVisible(true);
						textboxNama.setVisible(true);
						
						labelNpm.setVisible(false);
						labelNama.setVisible(false);
						
						textboxNpm.setValue(objectMahasiswa.getNpm());
						textboxNama.setValue(objectMahasiswa.getNama());
					}					
				});
			}
		};
	}

	
	@Command
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void doNew(){
		ListModelList listModelList = (ListModelList) listboxMahasiswa.getModel();
		listModelList.add(0, mahasiswa);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void doDelete(){
		Messagebox.show("Do you really want to remove item?", "Confirm", Messagebox.OK | Messagebox.CANCEL, Messagebox.EXCLAMATION, new EventListener() {
		 	@Override
		    public void onEvent(Event event) throws Exception {    	
		 		if (((Integer) event.getData()).intValue() == Messagebox.OK) {
		 			for(Mahasiswa mahasiswa : mahasiswas){
		 				mahasiswaService.delete(mahasiswa);
		 			}
		 			
		 			BindUtils.postGlobalCommand(null, null, "refreshAfterSave", null);
		 		}		
		 	}
	     });
	}
	
	@GlobalCommand
	@NotifyChange("mahasiswas")
	public void refreshAfterSave(){
		mahasiswas = mahasiswaService.getAllMahasiswas();
	}
}