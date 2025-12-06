package br.com.moveon.services;

import br.com.moveon.services.utils.Logger;

public abstract class AbstractService {
    protected final Logger logger;

    public AbstractService() {
        this.logger = Logger.getInstance();
    }
}
