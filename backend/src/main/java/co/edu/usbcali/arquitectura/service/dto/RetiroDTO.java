package co.edu.usbcali.arquitectura.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link co.edu.usbcali.arquitectura.domain.Retiro} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RetiroDTO implements Serializable {

    private Long id;

    @NotNull
    private String contiene;

    @NotNull
    private String estado;

    @NotNull
    private Double cantidad;

    @NotNull
    private Instant fecha;

    private ATMDTO atm;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContiene() {
        return contiene;
    }

    public void setContiene(String contiene) {
        this.contiene = contiene;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public Instant getFecha() {
        return fecha;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public ATMDTO getAtm() {
        return atm;
    }

    public void setAtm(ATMDTO atm) {
        this.atm = atm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RetiroDTO)) {
            return false;
        }

        RetiroDTO retiroDTO = (RetiroDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, retiroDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RetiroDTO{" +
            "id=" + getId() +
            ", contiene='" + getContiene() + "'" +
            ", estado='" + getEstado() + "'" +
            ", cantidad=" + getCantidad() +
            ", fecha='" + getFecha() + "'" +
            ", atm=" + getAtm() +
            "}";
    }
}
