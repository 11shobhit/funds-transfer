package request;

import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "transaction")
public class FundsTransferRequest {

	@XmlElement(name = "toUserId")
	private int toUserId;

	@XmlElement(name = "fromUserId")
	private int fromUserId;

	@XmlElement(name = "amount")
	private AtomicInteger amount;

	public int getToUserId() {
		return toUserId;
	}

	public void setToUserId(int toUserId) {
		this.toUserId = toUserId;
	}

	public int getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(int fromUserId) {
		this.fromUserId = fromUserId;
	}

	public AtomicInteger getAmount() {
		return amount;
	}

	public void setAmount(AtomicInteger amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "FundsTransferRequest{" + "toUserId=" + toUserId + ", fromUserId=" + fromUserId + ", amount=" + amount
				+ '}';
	}
}
