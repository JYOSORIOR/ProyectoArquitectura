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
 * A ATM.
 */
@Entity
@Table(name = "atm")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ATM implements Serializable {

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
    @Column(name = "tipo", nullable = false)
    private String tipo;

    @NotNull
    @Column(name = "saldodisponible", nullable = false)
    private Double saldodisponible;

    @NotNull
    @Column(name = "ubicacion", nullable = false)
    private String ubicacion;

    @OneToMany(mappedBy = "atm")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cuentas", "atm" }, allowSetters = true)
    private Set<Retiro> retiros = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "atms", "cuenta" }, allowSetters = true)
    private Transaccion transacciones;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ATM id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEstado() {
        return this.estado;
    }

    public ATM estado(String estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTipo() {
        return this.tipo;
    }

    public ATM tipo(String tipo) {
        this.setTipo(tipo);
        return this;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getSaldodisponible() {
        return this.saldodisponible;
    }

    public ATM saldodisponible(Double saldodisponible) {
        this.setSaldodisponible(saldodisponible);
        return this;
    }

    public void setSaldodisponible(Double saldodisponible) {
        this.saldodisponible = saldodisponible;
    }

    public String getUbicacion() {
        return this.ubicacion;
    }

    public ATM ubicacion(String ubicacion) {
        this.setUbicacion(ubicacion);
        return this;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Set<Retiro> getRetiros() {
        return this.retiros;
    }

    public void setRetiros(Set<Retiro> retiros) {
        if (this.retiros != null) {
            this.retiros.forEach(i -> i.setAtm(null));
        }
        if (retiros != null) {
            retiros.forEach(i -> i.setAtm(this));
        }
        this.retiros = retiros;
    }

    public ATM retiros(Set<Retiro> retiros) {
        this.setRetiros(retiros);
        return this;
    }

    public ATM addRetiros(Retiro retiro) {
        this.retiros.add(retiro);
        retiro.setAtm(this);
        return this;
    }

    public ATM removeRetiros(Retiro retiro) {
        this.retiros.remove(retiro);
        retiro.setAtm(null);
        return this;
    }

    public Transaccion getTransacciones() {
        return this.transacciones;
    }

    public void setTransacciones(Transaccion transaccion) {
        this.transacciones = transaccion;
    }

    public ATM transacciones(Transaccion transaccion) {
        this.setTransacciones(transaccion);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ATM)) {
            return false;
        }
        return id != null && id.equals(((ATM) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ATM{" +
            "id=" + getId() +
            ", estado='" + getEstado() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", saldodisponible=" + getSaldodisponible() +
            ", ubicacion='" + getUbicacion() + "'" +
            "}";
    }
}
