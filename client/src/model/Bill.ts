export class Bill {
  id: number;
  amount: number;
  categoryId: number;
  date: Date;
  description: string;
  paid: boolean
  ownAmount: number;
  ownerId: number;
  ownerName: string;

  constructor(amount: number, categoryId: number, description: string, ownAmount: number, ownerId: number, ownerName: string, paid = false) {
    this.amount = amount;
    this.categoryId = categoryId;
    this.description = description;
    this.ownAmount = ownAmount;
    this.date = new Date();
    this.paid = paid;
    this.ownerId = ownerId;
    this.ownerName = ownerName;
  }
  
  setId(id: number) {
    this.id = id;
  }
  
  setDate(date: Date) {
    this.date = date;
  }
  
}
