import { Address } from './entity/address'
import { Customer } from './entity/customer'
import { Order } from './entity/order'
import { OrderItem } from './entity/order-item'

const customer = new Customer('1', 'Thiago Resende')
const address = new Address('Street 1', 1, 'Zipcode 1', 'City 1')
customer.changeAddress(address)
customer.activate()
console.log(customer)

const item = new OrderItem('i1', 'Item 1', 100, 'p1', 2)
const item2 = new OrderItem('i2', 'Item 2', 200, 'p2', 2)
const order = new Order('o1', 'c1', [item, item2])
console.log(order)
