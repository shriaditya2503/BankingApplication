package com.project.BankingApplication.utils;

import com.project.BankingApplication.entity.Transaction;
import com.project.BankingApplication.entity.User;

public class EmailBody {

    // user created
    public static String createUserBody(User user) {
        return "Dear " + user.getFirstName()+" "+user.getLastName() + ",\n" +
                "\n" +
                "Thank you for choosing Lala Finance Bank! Your account has been successfully created. \n" +
                "\n" +
                "**Account Summary:**  \n" +
                "- Customer Name: " + user.getFirstName()+" "+user.getLastName() + "\n" +
                "- Account Number: " + user.getAccountNum() + "\n" +
                "- Account Type: SAVINGS \n" +
                "- Registered Mobile: " + user.getPhoneNum() + "\n" +
                "- Registered Email: " + user.getEmail() + "\n" +
                "\n" +
                "You can now enjoy seamless banking services, including fund transfers, account management, and more — right from your fingertips.\n" +
                "\n" +
                "If you have any questions or need assistance, feel free to reach out to our support team.\n" +
                "\n" +
                "Welcome aboard!  \n" +
                "Warm regards,  \n" +
                "Lala Finance Bank ";
    }

    // update details
    public static String updateDetails(User user) {
        return "Dear " + user.getFirstName()+" "+user.getLastName() +",\n" +
                "\n" +
                "We would like to inform you that your customer details have been successfully updated on " + user.getModificationDate().toLocalDate() + ".\n" +
                "\n" +
                "**Updated Fields (if applicable):**  \n" +
                "- Mobile Number: " + user.getPhoneNum() + "\n" +
                "- Email Address: " + user.getEmail() + "\n" +
                "- Address: Null  \n" +
                "\n" +
                "If you did not request this change or notice any unauthorized updates, please contact us immediately.\n" +
                "\n" +
                "Thank you for keeping your information up-to-date.\n" +
                "\n" +
                "Sincerely,  \n" +
                "Lala Finance Bank ";
    }

    // credit fund body
    public static String creditBody(User user, Transaction transaction, String fromName) {
        return "Dear " + user.getFirstName() + " " + user.getLastName() + ",\n" +
                "\n" +
                "An amount of ₹" + transaction.getAmount() + " has been credited to your account. \n" +
                "\n" +
                "**Transaction Details:**  \n" +
                "- Amount: ₹" + transaction.getAmount() + "\n" +
                "- Date: " + transaction.getTimeStamp() + "\n" +
                "- Transaction Type: CREDIT  \n" +
                "- Description: From " + fromName + "\n" +
                "- Available Balance: " + user.getBalance() + "\n" +
                "\n" +
                "Thank you for banking with us.\n" +
                "\n" +
                "Warm regards,  \n" +
                "Lala Finance Bank ";
    }

    // debit fund body
    public static String debitBody(User user, Transaction transaction) {
        return "Dear " + user.getFirstName() + " " + user.getLastName() + ",\n" +
                "\n" +
                "We would like to inform you that an amount of ₹" + transaction.getAmount() + " has been debited from your account. \n" +
                "\n" +
                "**Transaction Details:**  \n" +
                "- Amount: ₹" + transaction.getAmount() + "\n" +
                "- Date: " + transaction.getTimeStamp() + "\n" +
                "- Transaction Type: DEBIT  \n" +
                "- Description: Self Withdrawal \n" +
                "- Available Balance: " + user.getBalance() + "\n" +
                "\n" +
                "If this transaction was not initiated by you, please contact our support team immediately.\n" +
                "\n" +
                "Thank you,  \n" +
                "Lala Finance Bank ";
    }

    // fund transfer body
    public static String fundTransferBody(User user, Transaction fromTransaction, String toName) {
        return "Dear " + user.getFirstName() + " " + user.getLastName() + ",\n" +
                "\n" +
                "Your fund transfer of ₹" + fromTransaction.getAmount() + " to " + toName +" has been successfully processed. \n" +
                "\n" +
                "**Transaction Details:**  \n" +
                "- Amount: ₹" + fromTransaction.getAmount() + "\n" +
                "- Date: " + fromTransaction.getTimeStamp() + "\n" +
                "- Transaction Type: DEBIT  \n" +
                "- Description: Transferred to " + toName + "\n" +
                "- Available Balance: " + user.getBalance() + "\n" +
                "\n" +
                "If this transaction was not initiated by you, please contact our support team immediately.\n" +
                "\n" +
                "Thank you,  \n" +
                "Lala Finance Bank ";
    }

}
