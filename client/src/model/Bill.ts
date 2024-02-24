export class Bill {
  id: string;
  amount: number;
  category: string;
  date: Date;
  description: string;
  isPaid: boolean
  ownAmount: number;
  owner: string;

  constructor(amount: number, category: string, description: string, ownAmount: number, owner: string) {
    this.amount = amount;
    this.category = category;
    this.description = description;
    this.ownAmount = ownAmount;
    this.date = new Date();
    this.isPaid = false;
    this.owner = owner;
  }
}
