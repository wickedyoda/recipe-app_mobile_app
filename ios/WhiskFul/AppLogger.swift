import Foundation

class AppLogger {
    static let shared = AppLogger()
    private let tag = "WiskFul"
    private let logFileName = "wiskful-app.log"
    private let maxLogSize: UInt64 = 10 * 1024 * 1024 // 10 MB
    private var logFileURL: URL?
    
    private init() {
        setupLogFile()
    }
    
    private func setupLogFile() {
        guard let docs = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first else { return }
        logFileURL = docs.appendingPathComponent(logFileName)
        trimIfNeeded()
    }
    
    private func trimIfNeeded() {
        guard let file = logFileURL,
              let size = try? file.resourceValues(forKeys: [.fileSizeKey]).fileSize,
              size > maxLogSize else { return }
        
        do {
            let text = try String(contentsOf: file, encoding: .utf8)
            let cutoff = text.count / 2
            let half = String(text.suffix(text.count - cutoff))
            try half.write(to: file, atomically: true, encoding: .utf8)
        } catch {
            NSLog("[\(tag)] Failed to trim log: \(error)")
        }
    }
    
    private func write(level: String, message: String) {
        guard let file = logFileURL else { return }
        
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd HH:mm:ss.SSS"
        let timestamp = formatter.string(from: Date())
        let line = "\(timestamp) \(level): \(message)\n"
        
        do {
            if FileManager.default.fileExists(atPath: file.path) {
                let handle = try FileHandle(forWriting(to: file))
                defer { try? handle.close() }
                if let data = line.data(using: .utf8) {
                    try handle.seekToEnd()
                    handle.write(data)
                }
            } else {
                try line.write(to: file, atomically: true, encoding: .utf8)
            }
        } catch {
            NSLog("[\(tag)] Failed to write log: \(error)")
        }
    }
    
    func d(_ message: String) { NSLog("[\(tag)] DEBUG: \(message)"); write(level: "DEBUG", message: message) }
    func i(_ message: String) { NSLog("[\(tag)] INFO: \(message)"); write(level: "INFO", message: message) }
    func w(_ message: String) { NSLog("[\(tag)] WARN: \(message)"); write(level: "WARN", message: message) }
    func e(_ message: String) { NSLog("[\(tag)] ERROR: \(message)"); write(level: "ERROR", message: message) }
    
    var logFile: URL? { logFileURL }
}