package co.edu.usbcali.arquitectura.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link co.edu.usbcali.arquitectura.domain.ATM} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ATMDTO implements Serializable {

    private Long id;

    @NotNull
    private String estado;

    @NotNull
    private String tipo;

    @NotNull
    private Double saldodisponible;

    @NotNull
    private String ubicacion;

    private TransaccionDTO transacciones;

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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getSaldodisponible() {
        return saldodisponible;
    }

    public void setSaldodisponible(Double saldodisponible) {
        this.saldodisponible = saldodisponible;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public TransaccionDTO getTransacciones() {
        return transacciones;
    }

    public void setTransacciones(TransaccionDTO transacciones) {
        this.transacciones = transacciones;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ATMDTO)) {
            return false;
        }

        ATMDTO aTMDTO = (ATMDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, aTMDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ATMDTO{" +
            "id=" + getId() +
            ", estado='" + getEstado() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", saldodisponible=" + getSaldodisponible() +
            ", ubicacion='" + getUbicacion() + "'" +
            ", transacciones=" + getTransacciones() +
            "}";
    }
}
