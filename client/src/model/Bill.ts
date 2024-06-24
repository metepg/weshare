export class Bill {
  id: number;
  amount: number;
  category: number;
  date: Date;
  description: string;
  paid: boolean
  ownAmount: number;
  owner: string;

  constructor(amount: number, category: number, description: string, ownAmount: number, owner: string) {
    this.amount = amount;
    this.category = category;
    this.description = description;
    this.ownAmount = ownAmount;
    this.date = new Date();
    this.paid = false;
    this.owner = owner;
  }
}
