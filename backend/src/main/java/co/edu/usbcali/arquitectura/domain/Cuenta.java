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
 * A Cuenta.
 */
@Entity
@Table(name = "cuenta")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Cuenta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "tipo", nullable = false)
    private String tipo;

    @NotNull
    @Column(name = "estado", nullable = false)
    private String estado;

    @NotNull
    @Column(name = "saldo", nullable = false)
    private Double saldo;

    @OneToMany(mappedBy = "cuenta")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "atms", "cuenta" }, allowSetters = true)
    private Set<Transaccion> transacciones = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "cuentas" }, allowSetters = true)
    private Cliente cliente;

    @ManyToOne
    @JsonIgnoreProperties(value = { "cuentas", "atm" }, allowSetters = true)
    private Retiro retiros;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cuenta id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return this.tipo;
    }

    public Cuenta tipo(String tipo) {
        this.setTipo(tipo);
        return this;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEstado() {
        return this.estado;
    }

    public Cuenta estado(String estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Double getSaldo() {
        return this.saldo;
    }

    public Cuenta saldo(Double saldo) {
        this.setSaldo(saldo);
        return this;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public Set<Transaccion> getTransacciones() {
        return this.transacciones;
    }

    public void setTransacciones(Set<Transaccion> transaccions) {
        if (this.transacciones != null) {
            this.transacciones.forEach(i -> i.setCuenta(null));
        }
        if (transaccions != null) {
            transaccions.forEach(i -> i.setCuenta(this));
        }
        this.transacciones = transaccions;
    }

    public Cuenta transacciones(Set<Transaccion> transaccions) {
        this.setTransacciones(transaccions);
        return this;
    }

    public Cuenta addTransacciones(Transaccion transaccion) {
        this.transacciones.add(transaccion);
        transaccion.setCuenta(this);
        return this;
    }

    public Cuenta removeTransacciones(Transaccion transaccion) {
        this.transacciones.remove(transaccion);
        transaccion.setCuenta(null);
        return this;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Cuenta cliente(Cliente cliente) {
        this.setCliente(cliente);
        return this;
    }

    public Retiro getRetiros() {
        return this.retiros;
    }

    public void setRetiros(Retiro retiro) {
        this.retiros = retiro;
    }

    public Cuenta retiros(Retiro retiro) {
        this.setRetiros(retiro);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cuenta)) {
            return false;
        }
        return id != null && id.equals(((Cuenta) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cuenta{" +
            "id=" + getId() +
            ", tipo='" + getTipo() + "'" +
            ", estado='" + getEstado() + "'" +
            ", saldo=" + getSaldo() +
            "}";
    }
}
