package co.edu.usbcali.arquitectura.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Transaccion.
 */
@Entity
@Table(name = "transaccion")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Transaccion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "estado", nullable = false)
    private String estado;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private Instant fecha;

    @NotNull
    @Column(name = "cantidad", nullable = false)
    private Double cantidad;

    @OneToMany(mappedBy = "transacciones")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "retiros", "transacciones" }, allowSetters = true)
    private Set<ATM> atms = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "transacciones", "cliente", "retiros" }, allowSetters = true)
    private Cuenta cuenta;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Transaccion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEstado() {
        return this.estado;
    }

    public Transaccion estado(String estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Instant getFecha() {
        return this.fecha;
    }

    public Transaccion fecha(Instant fecha) {
        this.setFecha(fecha);
        return this;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public Double getCantidad() {
        return this.cantidad;
    }

    public Transaccion cantidad(Double cantidad) {
        this.setCantidad(cantidad);
        return this;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public Set<ATM> getAtms() {
        return this.atms;
    }

    public void setAtms(Set<ATM> aTMS) {
        if (this.atms != null) {
            this.atms.forEach(i -> i.setTransacciones(null));
        }
        if (aTMS != null) {
            aTMS.forEach(i -> i.setTransacciones(this));
        }
        this.atms = aTMS;
    }

    public Transaccion atms(Set<ATM> aTMS) {
        this.setAtms(aTMS);
        return this;
    }

    public Transaccion addAtm(ATM aTM) {
        this.atms.add(aTM);
        aTM.setTransacciones(this);
        return this;
    }

    public Transaccion removeAtm(ATM aTM) {
        this.atms.remove(aTM);
        aTM.setTransacciones(null);
        return this;
    }

    public Cuenta getCuenta() {
        return this.cuenta;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }

    public Transaccion cuenta(Cuenta cuenta) {
        this.setCuenta(cuenta);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Transaccion)) {
            return false;
        }
        return id != null && id.equals(((Transaccion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Transaccion{" +
            "id=" + getId() +
            ", estado='" + getEstado() + "'" +
            ", fecha='" + getFecha() + "'" +
            ", cantidad=" + getCantidad() +
            "}";
    }
}
