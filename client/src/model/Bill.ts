import { User } from './User';

export class Bill {
  id: number;
  amount: number;
  category: number;
  date: Date;
  description: string;
  paid: boolean
  ownAmount: number;
  owner: User;

  constructor(amount: number, category: number, description: string, ownAmount: number, owner: User) {
    this.amount = amount;
    this.category = category;
    this.description = description;
    this.ownAmount = ownAmount;
    this.date = new Date();
    this.paid = false;
    this.owner = owner;
  }
  
  setId(id: number) {
    this.id = id;
  }
  
  setDate(date: Date) {
    this.date = date;
  }
  
}
