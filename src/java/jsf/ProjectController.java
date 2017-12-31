package jsf;

import jpa.entities.Project;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import jpa.session.ProjectFacade;

import java.io.Serializable;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@Named("projectController")
@SessionScoped
public class ProjectController implements Serializable {

    private Project current;
    private DataModel items = null;
    @EJB
    private jpa.session.ProjectFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public ProjectController() {
    }

    public Project getSelected() {
        if (current == null) {
            current = new Project();
            current.setProjectPK(new jpa.entities.ProjectPK());
            selectedItemIndex = -1;
        }
        return current;
    }

    private ProjectFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (Project) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Project();
        current.setProjectPK(new jpa.entities.ProjectPK());
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            current.getProjectPK().setClientName(current.getClient().getClientPK().getClientName());
            current.getProjectPK().setClientDepartmentNumber(current.getClient().getClientPK().getClientDepartmentNumber());
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("ProjectCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Project) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            current.getProjectPK().setClientName(current.getClient().getClientPK().getClientName());
            current.getProjectPK().setClientDepartmentNumber(current.getClient().getClientPK().getClientDepartmentNumber());
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("ProjectUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Project) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("ProjectDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Project getProject(jpa.entities.ProjectPK id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Project.class)
    public static class ProjectControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ProjectController controller = (ProjectController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "projectController");
            return controller.getProject(getKey(value));
        }

        jpa.entities.ProjectPK getKey(String value) {
            jpa.entities.ProjectPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new jpa.entities.ProjectPK();
            key.setClientName(values[0]);
            key.setClientDepartmentNumber(Short.parseShort(values[1]));
            key.setProjectName(values[2]);
            return key;
        }

        String getStringKey(jpa.entities.ProjectPK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getClientName());
            sb.append(SEPARATOR);
            sb.append(value.getClientDepartmentNumber());
            sb.append(SEPARATOR);
            sb.append(value.getProjectName());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Project) {
                Project o = (Project) object;
                return getStringKey(o.getProjectPK());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Project.class.getName());
            }
        }

    }

}
