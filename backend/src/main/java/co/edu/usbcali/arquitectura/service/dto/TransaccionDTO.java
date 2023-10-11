package co.edu.usbcali.arquitectura.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link co.edu.usbcali.arquitectura.domain.Transaccion} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TransaccionDTO implements Serializable {

    private Long id;

    @NotNull
    private String estado;

    @NotNull
    private Instant fecha;

    @NotNull
    private Double cantidad;

    private CuentaDTO cuenta;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Instant getFecha() {
        return fecha;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public CuentaDTO getCuenta() {
        return cuenta;
    }

    public void setCuenta(CuentaDTO cuenta) {
        this.cuenta = cuenta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransaccionDTO)) {
            return false;
        }

        TransaccionDTO transaccionDTO = (TransaccionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, transaccionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransaccionDTO{" +
            "id=" + getId() +
            ", estado='" + getEstado() + "'" +
            ", fecha='" + getFecha() + "'" +
            ", cantidad=" + getCantidad() +
            ", cuenta=" + getCuenta() +
            "}";
    }
}
