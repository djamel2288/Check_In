package sample;

public class ModelTable {

    int id_p;
    String f_name_p;
    String l_name_p;
    String email_p;
    int nbr_p_p;

    public ModelTable(int id_p, String f_name_p, String l_name_p, String email_p, int nbr_p_p) {
        this.id_p = id_p;
        this.f_name_p = f_name_p;
        this.l_name_p = l_name_p;
        this.email_p = email_p;
        this.nbr_p_p = nbr_p_p;
    }

    public int getId_p() {
        return id_p;
    }

    public void setId_p(int id_p) {
        this.id_p = id_p;
    }

    public String getF_name_p() {
        return f_name_p;
    }

    public void setF_name_p(String f_name_p) {
        this.f_name_p = f_name_p;
    }

    public String getL_name_p() {
        return l_name_p;
    }

    public void setL_name_p(String l_name_p) {
        this.l_name_p = l_name_p;
    }

    public String getEmail_p() {
        return email_p;
    }

    public void setEmail_p(String email_p) {
        this.email_p = email_p;
    }

    public int getNbr_p_p() {
        return nbr_p_p;
    }

    public void setNbr_p_p(int nbr_p_p) {
        this.nbr_p_p = nbr_p_p;
    }
}
