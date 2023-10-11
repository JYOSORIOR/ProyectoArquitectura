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
 * A Retiro.
 */
@Entity
@Table(name = "retiro")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Retiro implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "contiene", nullable = false)
    private String contiene;

    @NotNull
    @Column(name = "estado", nullable = false)
    private String estado;

    @NotNull
    @Column(name = "cantidad", nullable = false)
    private Double cantidad;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private Instant fecha;

    @OneToMany(mappedBy = "retiros")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "transacciones", "cliente", "retiros" }, allowSetters = true)
    private Set<Cuenta> cuentas = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "retiros", "transacciones" }, allowSetters = true)
    private ATM atm;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Retiro id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContiene() {
        return this.contiene;
    }

    public Retiro contiene(String contiene) {
        this.setContiene(contiene);
        return this;
    }

    public void setContiene(String contiene) {
        this.contiene = contiene;
    }

    public String getEstado() {
        return this.estado;
    }

    public Retiro estado(String estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Double getCantidad() {
        return this.cantidad;
    }

    public Retiro cantidad(Double cantidad) {
        this.setCantidad(cantidad);
        return this;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public Instant getFecha() {
        return this.fecha;
    }

    public Retiro fecha(Instant fecha) {
        this.setFecha(fecha);
        return this;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public Set<Cuenta> getCuentas() {
        return this.cuentas;
    }

    public void setCuentas(Set<Cuenta> cuentas) {
        if (this.cuentas != null) {
            this.cuentas.forEach(i -> i.setRetiros(null));
        }
        if (cuentas != null) {
            cuentas.forEach(i -> i.setRetiros(this));
        }
        this.cuentas = cuentas;
    }

    public Retiro cuentas(Set<Cuenta> cuentas) {
        this.setCuentas(cuentas);
        return this;
    }

    public Retiro addCuenta(Cuenta cuenta) {
        this.cuentas.add(cuenta);
        cuenta.setRetiros(this);
        return this;
    }

    public Retiro removeCuenta(Cuenta cuenta) {
        this.cuentas.remove(cuenta);
        cuenta.setRetiros(null);
        return this;
    }

    public ATM getAtm() {
        return this.atm;
    }

    public void setAtm(ATM aTM) {
        this.atm = aTM;
    }

    public Retiro atm(ATM aTM) {
        this.setAtm(aTM);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Retiro)) {
            return false;
        }
        return id != null && id.equals(((Retiro) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Retiro{" +
            "id=" + getId() +
            ", contiene='" + getContiene() + "'" +
            ", estado='" + getEstado() + "'" +
            ", cantidad=" + getCantidad() +
            ", fecha='" + getFecha() + "'" +
            "}";
    }
}
