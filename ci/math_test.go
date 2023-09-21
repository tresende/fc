package main

func addTest(t *testing.T) int {
	total := add(1, 2)

	if total != 3 {
		t.Errorf("Incorrect sum, got: %d, want: %d.", total, 3)
	}
}