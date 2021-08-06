all: install

install:
	pushd docker > /dev/null 2>&1 && $(MAKE) $@

.DEFAULT: all

.PHONY: install