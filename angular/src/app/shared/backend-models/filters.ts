// FILTERS

import { OrderStatus } from '../view-models/interfaces';

export class Filter {
  pageable?: Pageable;
  isFav: boolean;
  searchBy: string;
  // sort: { name: string, direction: string }[];
  maxPrice: number;
  minLikes: number;
  categories: { id: string }[];
}

// export class FilterCockpit {
//   pageable?: Pageable;
//   // sort?: Sorting[];
//   bookingDate: string;
//   email: string;
//   bookingToken: number;
//   orderStatus?: OrderStatus[];
// }

export class FilterCockpit {
  pageable?: Pageable;
  inHouse?: boolean;
  bookingDate?: string;
  name?: string;
  email?: string;
  tableId?: number;
  bookingToken?: string;
  orderPayStatusId?: number;
  orderStatusId?: number;
  orderStatusIds?: number[];
}

// Filter User List
export class FilterAdmin {
  pageable?: Pageable;
  username: string;
  email: string;
  date?: string;
  userRoleId: number;
  bookingDate?: string;
}

export class Pageable {
  pageSize: number;
  pageNumber: number;
  sort?: Sort[];
}

export class Sort {
  property: string;
  direction: string;
}
