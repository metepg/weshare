import { Bill } from '../model/Bill';

export const MOCK_BILL: Bill = {
  id: 1,
  amount: 100,
  categoryId: 1,
  date: new Date(),
  description: 'Electricity Bill',
  paid: true,
  ownAmount: 50,
  ownerId: 101,
  ownerName: 'Tester',
} as Bill;
