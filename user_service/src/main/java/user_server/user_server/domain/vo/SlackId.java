package user_server.user_server.domain.vo;

import user_server.user_server.libs.error.BusinessException;
import user_server.user_server.libs.error.ErrorCode;

public class SlackId {

    String slackId;

    public SlackId(String slackId) {
        if (slackId == null || slackId.isEmpty()){
            throw new BusinessException(ErrorCode.INVALID_SLACK_ID);
        }
        this.slackId = slackId;
    }

    public static SlackId fromSlackId(String slackId) {
        return new SlackId(slackId);
    }
}
