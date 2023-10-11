package co.edu.usbcali.arquitectura.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Cliente.
 */
@Entity
@Table(name = "cliente")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotNull
    @Column(name = "tipodeusuario", nullable = false)
    private String tipodeusuario;

    @NotNull
    @Column(name = "correo", nullable = false)
    private String correo;

    @NotNull
    @Column(name = "cedula", nullable = false)
    private String cedula;

    @NotNull
    @Column(name = "telefono", nullable = false)
    private String telefono;

    @OneToMany(mappedBy = "cliente")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "transacciones", "cliente", "retiros" }, allowSetters = true)
    private Set<Cuenta> cuentas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cliente id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Cliente nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipodeusuario() {
        return this.tipodeusuario;
    }

    public Cliente tipodeusuario(String tipodeusuario) {
        this.setTipodeusuario(tipodeusuario);
        return this;
    }

    public void setTipodeusuario(String tipodeusuario) {
        this.tipodeusuario = tipodeusuario;
    }

    public String getCorreo() {
        return this.correo;
    }

    public Cliente correo(String correo) {
        this.setCorreo(correo);
        return this;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCedula() {
        return this.cedula;
    }

    public Cliente cedula(String cedula) {
        this.setCedula(cedula);
        return this;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public Cliente telefono(String telefono) {
        this.setTelefono(telefono);
        return this;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Set<Cuenta> getCuentas() {
        return this.cuentas;
    }

    public void setCuentas(Set<Cuenta> cuentas) {
        if (this.cuentas != null) {
            this.cuentas.forEach(i -> i.setCliente(null));
        }
        if (cuentas != null) {
            cuentas.forEach(i -> i.setCliente(this));
        }
        this.cuentas = cuentas;
    }

    public Cliente cuentas(Set<Cuenta> cuentas) {
        this.setCuentas(cuentas);
        return this;
    }

    public Cliente addCuenta(Cuenta cuenta) {
        this.cuentas.add(cuenta);
        cuenta.setCliente(this);
        return this;
    }

    public Cliente removeCuenta(Cuenta cuenta) {
        this.cuentas.remove(cuenta);
        cuenta.setCliente(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cliente)) {
            return false;
        }
        return id != null && id.equals(((Cliente) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cliente{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", tipodeusuario='" + getTipodeusuario() + "'" +
            ", correo='" + getCorreo() + "'" +
            ", cedula='" + getCedula() + "'" +
            ", telefono='" + getTelefono() + "'" +
            "}";
    }
}
