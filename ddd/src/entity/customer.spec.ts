import { Address } from './address'
import { Customer } from './customer'

describe('Customer', () => {
  it('should throw error when id is empty', () => {
    expect(() => new Customer('', 'Thiago Resende')).toThrowError('Id is required')
  })

  it('should throw error when name is empty', () => {
    expect(() => new Customer('123', '')).toThrowError('Name is required')
  })

  it('should change name', () => {
    const customer = new Customer('123', 'Thiago Resende')
    customer.changeName('ABC')

    expect(customer.name).toBe('ABC')
  })

  it('should activate customer', () => {
    const customer = new Customer('1', 'Customer 1')
    const address = new Address('Street 1', 1, 'Zipcode 1', 'City 1')

    customer.changeAddress(address)
    customer.activate()

    expect(customer.isActive()).toBeTruthy()
  })

  it('should throw error when address is undefined', () => {
    const customer = new Customer('1', 'Customer 1')

    expect(() => customer.activate()).toThrowError('Address is mandatory to activate a customer')
  })

  it('should deactivate customer', () => {
    const customer = new Customer('1', 'Customer 1')

    customer.deactivate()

    expect(customer.isActive()).toBeFalsy()
  })
})
