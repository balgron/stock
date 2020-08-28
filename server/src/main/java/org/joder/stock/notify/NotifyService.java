package org.joder.stock.notify;

import org.joder.stock.notify.domain.Message;

/**
 * @author Joder 2020/8/26 23:22
 */
public interface NotifyService {

    boolean notifyMessage(Message message);
}
