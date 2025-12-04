package com.acuario.hotel.dto;

import com.acuario.hotel.model.TipoDocumento;
import jakarta.validation.constraints.*;

public class ClienteRequestDTO {

	@NotBlank(message = "El nombre es obligatorio")
	@Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
	private String nombre;

	@NotBlank(message = "El apellido es obligatorio")
	@Size(max = 100, message = "El apellido no puede exceder 100 caracteres")
	private String apellido;

	@NotNull(message = "El tipo de documento es obligatorio")
	private TipoDocumento tipoDocumento;

	@NotBlank(message = "El documento de identidad es obligatorio")
	@Size(min = 8, max = 20, message = "El documento de identidad debe tener entre 8 y 20 caracteres")
	private String documentoIdentidad;

	@Email(message = "El email debe tener un formato válido")
	@Size(max = 150, message = "El email no puede exceder 150 caracteres")
	private String email;

	@NotBlank(message = "El celular es obligatorio")
	@Size(min = 9, max = 9, message = "El celular debe tener 9 dígitos")
	private String celular;

	@Size(max = 200, message = "La dirección no puede exceder 200 caracteres")
	private String direccion;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getDocumentoIdentidad() {
		return documentoIdentidad;
	}

	public void setDocumentoIdentidad(String documentoIdentidad) {
		this.documentoIdentidad = documentoIdentidad;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
}



