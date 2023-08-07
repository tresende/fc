import { Address } from './address'

export class Customer {
  private readonly _id: string
  private _name: string
  private _address!: Address
  private _active = true

  constructor(id: string, name: string) {
    this._id = id
    this._name = name
    this.validate()
  }

  validate() {
    if (!this._name) {
      throw new Error('Name is required')
    }
    if (!this._id) {
      throw new Error('Id is required')
    }
  }

  isActivated(): boolean {
    return this._active
  }

  get name() {
    return this._name
  }

  changeAddress(address: Address) {
    this._address = address
  }

  changeName(name: string) {
    this._name = name
  }

  isActive(): boolean {
    return this._active
  }

  activate() {
    if (this._address === undefined) {
      throw new Error('Address is mandatory to activate a customer')
    }
    this._active = true
  }

  deactivate() {
    this._active = false
  }
}

const customer = new Customer('1', 'abc')
