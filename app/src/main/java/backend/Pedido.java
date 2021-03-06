package backend;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Pedido implements Serializable {

	private int id;
	private String email;
	private int telefono;
	private String direccionCliente;
	private String restaurante;
	private String direccionRestaurante;
	private ArrayList<String> platos;
	private double precioTotal;
	private String metodoPago;
	private String estado;
	private EstadoPedido estadoPedido;

	public Pedido(int id, String email, int telefono, String direccionCliente, String restaurante,
			String direccionRestaurante, ArrayList<String> platos, double precioTotal, String metodoPago,
			String estado) {
		this.id = id;
		this.email = email;
		this.telefono = telefono;
		this.direccionCliente = direccionCliente;
		this.restaurante = restaurante;
		this.direccionRestaurante = direccionRestaurante;
		this.platos = platos;
		this.precioTotal = precioTotal;
		this.metodoPago = metodoPago;
		this.estado = estado;

		estadoPedido = new EstadoPedidoPreparando();

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getTelefono() {
		return telefono;
	}

	public void setTelefono(int telefono) {
		this.telefono = telefono;
	}

	public String getDireccionCliente() {
		return direccionCliente;
	}

	public void setDireccionCliente(String direccionCliente) {
		this.direccionCliente = direccionCliente;
	}

	public String getRestaurante() {
		return restaurante;
	}

	public void setRestaurante(String restaurante) {
		this.restaurante = restaurante;
	}

	public String getDireccionRestaurante() {
		return direccionRestaurante;
	}

	public void setDireccionRestaurante(String direccionRestaurante) {
		this.direccionRestaurante = direccionRestaurante;
	}

	public ArrayList<String> getPlatos() {
		return platos;
	}

	public void setPlatos(ArrayList<String> platos) {
		this.platos = platos;
	}

	public double getPrecioTotal() {
		return precioTotal;
	}

	public void setPrecioTotal(double precioTotal) {
		this.precioTotal = precioTotal;
	}

	public String getMetodoPago() {
		return metodoPago;
	}

	public void setMetodoPago(String metodoPago) {
		this.metodoPago = metodoPago;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public EstadoPedido getEstadoPedido() {
		return estadoPedido;
	}

	public void setEstadoPedido(EstadoPedido estadoPedido) {
		this.estadoPedido = estadoPedido;
	}

	public void cancelar() {
		estadoPedido = new EstadoPedidoCancelado();
		estadoPedido.ejecutar(this);
	}

	public void completar() {
		estadoPedido = new EstadoPedidoCompletado();
		estadoPedido.ejecutar(this);
	}

	public void preparando() {
		estadoPedido = new EstadoPedidoPreparando();
		estadoPedido.ejecutar(this);
	}

	public abstract void añadirSubpedido(Pedido p);

	public abstract void eliminarSubpedido(Pedido p);

	public abstract String getDescripcion();

	public abstract double getPrecioPedido();

	@Override
	public String toString() {
		return "Pedido [id=" + id + ", email=" + email + ", telefono=" + telefono + ", direccionCliente="
				+ direccionCliente + ", restaurante=" + restaurante + ", direccionRestaurante=" + direccionRestaurante
				+ ", platos=" + platos + ", precioTotal=" + precioTotal + ", metodoPago=" + metodoPago + ", estado="
				+ estado + ", estadoPedido=" + estadoPedido + "]";
	}
	

}
