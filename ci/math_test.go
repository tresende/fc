package main

import "testing"

func TestAdd(t *testing.T) {

	total := add(15, 15)

	if total != 30 {
		t.Errorf("Incorrect sum, got: %d, want: %d.", total, 3)
	}
}
