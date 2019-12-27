package request;

import java.util.concurrent.atomic.AtomicInteger;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CreateUserRequest {
	public CreateUserRequest() {
	}

	private static final long serialVersionUID = 1L;
	public int id;

	@XmlElement(nillable = false)
	public String name;

	@NotNull
	public AtomicInteger balance;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AtomicInteger getBalance() {
		return balance;
	}

	public void setBalance(AtomicInteger balance) {
		this.balance = balance;
	}

	@Override
	public String toString() {
		return "CreateUserRequest{" + "id=" + id + ", name='" + name + '\'' + ", balance=" + balance.get() + '}';
	}
}
